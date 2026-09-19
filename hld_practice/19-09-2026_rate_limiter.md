# Distributed Rate Limiter - 19th Sep 2026

https://www.hellointerview.com/practice/system-design/cmmai8lk30rgk09adoawwe264

![19-09-2026_rate_limiter](images/19-09-2026_rate_limiter.png)

# FeedBack

1. **Requirements**
    1. A rate limiter sits in the critical path of every single request, so its latency budget must be under 5ms. 
    2. Distributed rate limiters should explicitly favor eventual consistency over strict consistency. Because the rate limiter runs across many nodes, request counts may be slightly out of sync between nodes at any given moment, and that is acceptable. Blocking a few extra requests or allowing a few extra through is far less harmful than slowing down or crashing the system to maintain perfect accuracy.
2. **Core Entities**
    1. A rate limiter has three core data entities: Clients (who is making requests, e.g. user ID, API key, IP), Rules (the policies defining how many requests a client can make in a time window), and Requests (the individual incoming calls that get counted against a client's limit). Missing any one of these means the system cannot function.
    2. Requests must be tracked as a core entity in a rate limiter because the system needs to count how many requests a client has made within a given time window. Without persisting or reasoning about individual requests, there is no way to enforce a rule like '100 requests per minute per user'.
3. **System Interface**
    1. A rate limiter should return three outputs: an allow/deny decision (boolean like isAllowed), the remaining request count in the current window, and the reset time (a timestamp telling the client when their limit resets). The reset time is critical because clients use it to know when to retry instead of hammering the server repeatedly.
    2. A rate limiter should return a boolean like isAllowed rather than an HTTP status code like 429. The rate limiter is a decision-making component, not an HTTP layer. The caller (like an API gateway) is responsible for translating that decision into an HTTP response.
    3. The rate limiter interface needs to know which rule or policy to apply, so the endpoint path or a ruleId should be an explicit input alongside the client identifier. Different API endpoints often have different rate limits, so without this the rate limiter cannot look up the correct policy.
4. **High Level Design**
    1. Rate limit algorithm (what & which to choose)
        1. Fixed Window
        2. Sliding Window
        3. Token Bucket
        4. Leaky Bucket
    2. API gateway placement depends on a fast shared store like Redis so that all gateway instances enforce the same global limit consistently. Without shared state, each gateway instance tracks limits independently and users can exceed the intended quota by hitting different instances.
    3. When a request is rate limited, return HTTP 429 with a Retry-After header telling the client how many seconds to wait. You can also include X-RateLimit-Limit, X-RateLimit-Remaining, and X-RateLimit-Reset headers to help clients understand their full quota state proactively, not just when they are denied.
5. **Deep Dives**
    1. When a consistent hashing rebalance moves a client key to a new shard, that new shard starts with a counter of zero, giving the client a fresh quota mid-window. The fix is to migrate the counter value from the old shard to the new one before flipping the routing, or temporarily treat the client as at their limit until the counter is confirmed on the new node.
    2. When Redis goes down, falling back to a global signal like server thread capacity does not enforce per-client fairness. One bad client can starve everyone else. The right fallback is a local in-memory cache of each client's last known counter on the gateway, intentionally set to a conservative fraction of the normal limit so that even without coordination between gateway nodes the aggregate traffic stays safe.
    3. To keep Redis round trips under a tight latency budget like 5ms, always use a persistent connection pool so you are not paying TCP handshake costs on every request. Also combine the counter check and increment into a single atomic operation using a Lua script or a Redis command like INCR with an expiry, so you avoid a second round trip.
