# HLD + LLD

  - [HLD - Mock Interview](#hld---mock-interview)
  - [Design Patterns](#design-patterns)
  - [Java Concurrency](#java-concurrency)
  - [LLD - Mock Interview](#lld---mock-interview)

## HLD - Mock Interview

1. Distributed Cache
2. Rate Limiter
3. Notification System
4. Job Scheduler
5. Ticketmaster
6. Facebook News Feed
7. WhatsApp
8. YouTube
9. Dropbox
10. Uber
11. Payment System
12. Google Docs
13. Web Crawler
14. Metrics Monitoring
15. Ad Click Aggregator
16. Instagram
17. LeetCode
18. Robinhood
19. ChatGPT
20. Flash Sale
21. Game Leaderboard
22. FB Live Comments
23. FB Post Search
24. Bitly
25. Yelp

## Design Patterns

1. Singleton
2. Factory
3. Strategy
4. Observer
5. State
6. Decorator
7. Command

## Java Concurrency

1. **Thread Creation & Lifecycle**
    - Thread / Runnable / Callable
    - `start()`, `join()`, `sleep()`, interruption
2. **synchronized + wait/notify**
    - `synchronized`
    - `wait()`, `notify()`, `notifyAll()`
    - Build **Producer-Consumer**
3. **volatile vs synchronized**
    - Visibility vs atomicity
    - Happens-before
    - Don't over-invest in formal JMM details
4. **Locks**
    - `ReentrantLock`
    - `Condition`
    - `ReadWriteLock`
5. **Atomic Classes + CAS**
    - `AtomicInteger`, `AtomicLong`, `AtomicReference`
    - CAS concept
    - `LongAdder` for high-contention counters
6. **ExecutorService / ThreadPoolExecutor**
    - Pool sizing
    - Work queues
    - Rejection policies
7. **Callable / Future / CompletableFuture**
    - Async execution
    - Chaining and combining tasks
    - Exception handling
8. **Concurrent Collections**
    - `ConcurrentHashMap`
    - `BlockingQueue`
    - `CopyOnWriteArrayList`
9. **Synchronization Utilities**
    - `CountDownLatch` → wait for tasks to complete
    - `CyclicBarrier` → multiple threads wait at a common point
    - `Semaphore` → limit concurrent access
10. **Deadlock / Livelock / Starvation**
    - What they are
    - Prevention
    - Lock ordering
11. **Coding Practice**
    - Bounded Blocking Queue
    - Producer-Consumer
    - Thread-safe LRU Cache
    - Rate Limiter
    - Print in Order / Ordered Thread Execution
12. **Thread-Safety Concepts**
    - Thread safety
    - Immutability
    - Thread confinement
    - Safe publication
    - Focus on concepts rather than code
13. **ThreadLocal**
    - Use cases
    - Why it can cause problems in thread pools
    - Cleanup with `remove()`
14. **Virtual Threads vs Platform Threads**
    - Key differences
    - When to use virtual threads
    - Thread pinning
    - Spend **15–20 minutes** here — important modern Java interview topic in 2026.

## LLD - Mock Interview

1. Parking Lot
2. Vending Machine
3. Elevator
4. BookMyShow
5. LRU Cache
6. Splitwise
7. Logger
8. Rate Limiter
9. Task Scheduler
10. Cab Booking
11. Notification
12. File System
13. Chess
14. ATM
15. Shopping / Order Management
