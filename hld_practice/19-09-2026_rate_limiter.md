# Distributed Rate Limiter

# 19th-Sep-2026 Feedback

https://www.hellointerview.com/practice/system-design/cmmai8lk30rgk09adoawwe264

![19-09-2026_rate_limiter](images/19-09-2026_rate_limiter.png)

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
  

# 1st-Oct-2026 Feedback

https://www.hellointerview.com/practice/system-design/cmupehus30wpt01adwcplcdg5

![1-10-2026_rate_limiter](images/1-10-2026_rate_limiter.png)

1. **Requirements**
    1. A rate limiter sits in the critical path of every single request, so latency is a top-tier requirement. A good target to memorize is under 5ms added overhead per request check. If the rate limiter is slow, it slows down your entire product.
2. **Core Entities**
    1. In a rate limiter design, always include 'client' as its own entity separate from requests and rules. A client represents the identity being tracked and limited, whether identified by user ID, API key, or IP address. Rules define the limits, requests carry the identifying info, but the client is what the system actually counts traffic for and checks against those rules.
    2. Think of the three core entities in a rate limiter as a triangle: the Rule (defines the limit, e.g. 100 requests per minute), the Client (the identity being limited), and the Request (the incoming call that carries the client identity). Each plays a distinct role and leaving one out makes your data model incomplete.
3. **System Interface**
    1. Always return the remaining request count on every response, not just when allowed. This lets well-behaved clients throttle themselves before hitting the limit, and it maps directly to the standard X-RateLimit-Remaining HTTP header that real APIs use.
    2. Always return the window reset time on every response, not just when denied. Clients need to know when their quota refreshes even on successful requests, and it maps to the standard X-RateLimit-Reset header.
    3. The input to a rate limiter should be a small identifier like a ruleId or endpoint path, not the full policy object. The limiter owns its own rules in config and just needs to know which rule to look up. A clean signature looks like isRequestAllowed(clientId, ruleId).
    4. Different endpoints often have different rate limits, so the rule selector in the input is what tells the limiter which limit to enforce. Without it, the limiter has no way to apply the right quota for a given request.
4. **High Level Design**
    1. When returning rate limit headers, go beyond just the 429 status and Retry-After header. Include X-RateLimit-Limit (the max allowed), X-RateLimit-Remaining (how many requests are left in the window), and X-RateLimit-Reset (when the limit resets). These give client developers a clear contract so they can manage their own request pacing proactively.
5. **Deep Dives**
    1. To minimize Redis round trip latency, use a Lua script or a Redis atomic command that does the entire rate limit check and increment in a single server-side operation. This avoids multiple client-server round trips where each extra hop can add 1-2ms and blow your latency budget.
    2. Co-locate your gateway instances and Redis shards in the same region and availability zone. Even with connection pooling, a cross-region Redis call can add 20-100ms of network latency, which makes a 5ms budget impossible to hit regardless of how fast Redis processes the request.
    3. Connection pooling removes TCP handshake and TLS setup overhead from the hot path by reusing long-lived connections. Without it, each rate limit check pays a connection setup cost of several milliseconds before the Redis operation even starts.
    4. When reasoning about a latency budget like 5ms, split it into components explicitly. For example, say '1ms for the network hop to Redis within the same region, plus under 1ms for the Redis operation itself, leaving buffer for gateway processing.' This shows you are reasoning from real numbers rather than just saying Redis is fast.

