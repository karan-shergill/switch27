# Senior SDE HLD Concept Checklist

A preparation checklist for Senior Software Development Engineer system design interviews (around 8 years of experience).

Use this as a master checklist. Prioritize architectural decisions, failure scenarios, scalability bottlenecks, and trade-offs over memorizing definitions.

## 1. Distributed Systems Fundamentals

**Priority: P0 — Very high**

1. CAP Theorem
2. PACELC Theorem
3. Strong Consistency vs Eventual Consistency
4. Linearizability vs Sequential Consistency
5. Distributed Transactions
6. Two-Phase Commit (2PC)
7. Saga Pattern
8. Transactional Outbox Pattern
9. Idempotency
10. Deduplication
11. At-Most-Once vs At-Least-Once vs Exactly-Once Delivery
12. Distributed Locking
13. Leader Election
14. Split Brain Problem
15. Clock Synchronization and Clock Skew

## 2. Database Scaling and Architecture

**Priority: P0 — Very high**

1. Database Sharding
2. Horizontal vs Vertical Partitioning
3. Sharding Strategies (Hash, Range, Directory-based)
4. Consistent Hashing
5. Hot Partitions / Hot Keys
6. Database Replication
7. Primary-Replica Architecture
8. Synchronous vs Asynchronous Replication
9. Replication Lag
10. Read Replicas
11. Database Failover
12. Database Indexing (B-Tree, Hash Index)
13. Composite Indexes
14. SQL vs NoSQL
15. Choosing between SQL, Document, Key-Value, Wide-Column and Graph Databases
16. Database Connection Pooling

## 3. Caching

**Priority: P0 — Very high**

1. Cache-Aside Pattern
2. Read-Through Cache
3. Write-Through Cache
4. Write-Behind / Write-Back Cache
5. Write-Around Cache
6. Cache Invalidation Strategies
7. TTL-based Expiration
8. Cache Eviction Policies (LRU, LFU)
9. Cache Stampede
10. Cache Penetration
11. Cache Avalanche
12. Hot Keys
13. Distributed Cache Architecture
14. Redis Replication and Redis Cluster

## 4. Message Queues and Event-Driven Architecture

**Priority: P0 — Very high**

1. Kafka Architecture
2. Kafka Topics and Partitions
3. Kafka Consumer Groups
4. Consumer Offsets
5. Message Ordering
6. Partition Key Selection
7. Consumer Rebalancing
8. Message Retry Strategies
9. Dead Letter Queue (DLQ)
10. Backpressure
11. Event-Driven Architecture
12. Event Sourcing
13. CQRS
14. Stream Processing
15. Change Data Capture (CDC)
16. Kafka vs RabbitMQ vs SQS

## 5. High Availability and Reliability

**Priority: P0 — Very high**

1. Load Balancing (L4 vs L7)
2. Active-Active vs Active-Passive Architecture
3. Health Checks
4. Heartbeats
5. Circuit Breaker Pattern
6. Retry with Exponential Backoff
7. Retry with Jitter
8. Timeout Management
9. Bulkhead Pattern
10. Graceful Degradation
11. Fault Tolerance
12. Disaster Recovery
13. RPO and RTO
14. Multi-AZ vs Multi-Region Deployment
15. Failover Strategies

## 6. Scalability and Performance

**Priority: P0 — Very high**

1. Horizontal vs Vertical Scaling
2. Stateless vs Stateful Services
3. CDN Architecture
4. Reverse Proxy
5. API Gateway
6. Rate Limiting Algorithms (Token Bucket, Leaky Bucket, Sliding Window)
7. Fanout on Write vs Fanout on Read
8. Data Hotspots
9. Pagination (Offset vs Cursor-based)
10. Database Query Optimization
11. Connection Pooling
12. Throughput vs Latency
13. Tail Latency (P95, P99)
14. Capacity Estimation and Traffic Estimation

## 7. Real-Time Systems

**Priority: P1 — High**

1. WebSockets
2. Server-Sent Events (SSE)
3. Long Polling vs Short Polling
4. WebSocket Scaling
5. Pub/Sub Architecture
6. Real-Time Fanout
7. Presence Management
8. Heartbeat and Connection Management
9. WebSocket Connection Routing
10. Handling Millions of Concurrent Connections

## 8. Search and Data Processing

**Priority: P1 — High**

1. Inverted Index
2. Elasticsearch Architecture
3. Full-Text Search
4. Geospatial Indexing (Geohash, QuadTree, R-Tree)
5. Batch Processing vs Stream Processing
6. Apache Spark Fundamentals
7. Apache Flink Fundamentals
8. OLTP vs OLAP
9. Data Aggregation
10. Materialized Views

## 9. Security and API Design

**Priority: P1 — High**

1. Authentication vs Authorization
2. OAuth 2.0
3. JWT
4. API Key Authentication
5. TLS / HTTPS
6. Encryption at Rest vs In Transit
7. Secrets Management
8. API Versioning
9. Idempotency Keys
10. Request Validation
11. Pagination and Filtering
12. Audit Logging

## 10. Observability and Monitoring

**Priority: P1 — High**

1. Logs, Metrics and Distributed Tracing
2. Correlation IDs
3. OpenTelemetry Fundamentals
4. RED Metrics (Rate, Errors, Duration)
5. USE Metrics (Utilization, Saturation, Errors)
6. SLI, SLO and SLA
7. Alerting and Monitoring
8. Distributed Request Tracing
9. Error Budgets

## Recommended Preparation Order

| Priority | Sections | Preparation focus |
|---|---|---|
| P0 | 1–6 | Master thoroughly |
| P1 | 7–10 | Understand architecture, practical implementation, and trade-offs |

## What Does Mastery Mean?

For every concept, make sure you can independently answer these four questions:

1. **What?** Explain the concept and why it exists.
2. **How?** Explain its internal working and architecture.
3. **When?** Explain when to use it and when not to use it.
4. **Trade-offs?** Explain its advantages, disadvantages, and alternatives.

### Example: Kafka Partitioning

You should be able to explain:

- How partitions enable parallelism.
- How partition keys affect message ordering.
- What happens when consumers rebalance.
- How to identify and handle hot partitions.

## Preparation Advice

You do not need to study every bullet at textbook depth. For Senior SDE interviews, focus on:

- Architectural decisions and why you chose them.
- Failure scenarios and recovery strategies.
- Scalability bottlenecks and how to remove them.
- Consistency, availability, latency, and cost trade-offs.
- The operational impact of your design choices.
