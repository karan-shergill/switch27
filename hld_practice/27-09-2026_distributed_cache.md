Distributed Cache - 27th Sep 2026

https://www.hellointerview.com/practice/system-design/cmujc6yf84n4p08adwszs7v4z

![27-09-2026_distributed_cache](images/27-09-2026_distributed_cache.png)

# FeedBack

1. **Requirements**
    1. For a distributed cache, low latency is the primary non-functional requirement because the entire purpose of a cache is to serve data faster than the underlying database.
2. **High Level Design**
    1. Always include both lazy and eager expiration in a TTL cache. Lazy deletion means every get call checks the stored timestamp against the current time and treats expired entries as missing. Eager deletion means a background janitor process periodically scans and removes stale entries. You need both because the janitor may not have run yet when a client reads a key.
    2. In an LRU cache, both reads and writes should move an entry to the head of the doubly linked list, not just writes. LRU stands for least recently used, so any access counts as recent use and should refresh the entry's position.
    3. When describing a cache backed by a hash map plus doubly linked list, explicitly state that get, set, move to head, and tail eviction are all O(1). This is the main reason you pick this structure over simpler alternatives, and interviewers expect you to call it out.
    4. When describing a concurrent cache implementation, mention that reads and writes should be protected with a mutex or a read-write lock. A read-write lock is slightly better because it allows multiple concurrent reads while still blocking during writes.
3. **Deep Dives**
    1. In async replication, the primary accepts the write and returns success to the client immediately, then ships the update to replicas in the background. This keeps write latency low but means replicas may briefly serve stale data. For a cache this is usually fine because the database is the source of truth.
    2. Read-after-write consistency is the specific problem where a client writes to the primary and then reads from a replica that has not caught up yet, seeing stale data. To fix this you can route that client's reads to the primary for a short window after the write, or use a version or timestamp so replicas can reject reads until they are caught up. ⭐️
    3. When a replica falls behind or recovers from downtime it needs a defined catch-up path before it can safely rejoin. This usually means the replica requests the missed writes from the primary or a write-ahead log and applies them in order. Without this step replication is not truly fault tolerant because a recovered node could serve outdated data. ⭐️
    4. Consistent hashing minimizes data movement when scaling a cache cluster. Only the keys that fall between the old and new node boundaries need to move, instead of remapping all keys. During a node addition or removal you keep both nodes alive simultaneously so reads always hit at least one valid copy, preventing cache misses during the transition. ⭐️
