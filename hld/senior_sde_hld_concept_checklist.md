# Senior SDE HLD Concept Checklist

A preparation checklist for Senior Software Development Engineer system design interviews (around 8 years of experience).

Use this as a master checklist. Prioritize architectural decisions, failure scenarios, scalability bottlenecks, and trade-offs over memorizing definitions.

## 1. Distributed Systems Fundamentals

**Priority: P0 — Very high**

- [ ] CAP Theorem
- [ ] PACELC Theorem
- [ ] Strong Consistency vs Eventual Consistency
- [ ] Linearizability vs Sequential Consistency
- [ ] Distributed Transactions
- [ ] Two-Phase Commit (2PC)
- [ ] Saga Pattern
- [ ] Transactional Outbox Pattern
- [ ] Idempotency
- [ ] Deduplication
- [ ] At-Most-Once vs At-Least-Once vs Exactly-Once Delivery
- [ ] Distributed Locking
- [ ] Leader Election
- [ ] Split Brain Problem
- [ ] Clock Synchronization and Clock Skew

## 2. Database Scaling and Architecture

**Priority: P0 — Very high**

- [ ] Database Sharding
- [ ] Horizontal vs Vertical Partitioning
- [ ] Sharding Strategies (Hash, Range, Directory-based)
- [ ] Consistent Hashing
- [ ] Hot Partitions / Hot Keys
- [ ] Database Replication
- [ ] Primary-Replica Architecture
- [ ] Synchronous vs Asynchronous Replication
- [ ] Replication Lag
- [ ] Read Replicas
- [ ] Database Failover
- [ ] Database Indexing (B-Tree, Hash Index)
- [ ] Composite Indexes
- [ ] SQL vs NoSQL
- [ ] Choosing between SQL, Document, Key-Value, Wide-Column and Graph Databases
- [ ] Database Connection Pooling

## 3. Caching

**Priority: P0 — Very high**

- [ ] Cache-Aside Pattern
- [ ] Read-Through Cache
- [ ] Write-Through Cache
- [ ] Write-Behind / Write-Back Cache
- [ ] Write-Around Cache
- [ ] Cache Invalidation Strategies
- [ ] TTL-based Expiration
- [ ] Cache Eviction Policies (LRU, LFU)
- [ ] Cache Stampede
- [ ] Cache Penetration
- [ ] Cache Avalanche
- [ ] Hot Keys
- [ ] Distributed Cache Architecture
- [ ] Redis Replication and Redis Cluster

## 4. Message Queues and Event-Driven Architecture

**Priority: P0 — Very high**

- [ ] Kafka Architecture
- [ ] Kafka Topics and Partitions
- [ ] Kafka Consumer Groups
- [ ] Consumer Offsets
- [ ] Message Ordering
- [ ] Partition Key Selection
- [ ] Consumer Rebalancing
- [ ] Message Retry Strategies
- [ ] Dead Letter Queue (DLQ)
- [ ] Backpressure
- [ ] Event-Driven Architecture
- [ ] Event Sourcing
- [ ] CQRS
- [ ] Stream Processing
- [ ] Change Data Capture (CDC)
- [ ] Kafka vs RabbitMQ vs SQS

## 5. High Availability and Reliability

**Priority: P0 — Very high**

- [ ] Load Balancing (L4 vs L7)
- [ ] Active-Active vs Active-Passive Architecture
- [ ] Health Checks
- [ ] Heartbeats
- [ ] Circuit Breaker Pattern
- [ ] Retry with Exponential Backoff
- [ ] Retry with Jitter
- [ ] Timeout Management
- [ ] Bulkhead Pattern
- [ ] Graceful Degradation
- [ ] Fault Tolerance
- [ ] Disaster Recovery
- [ ] RPO and RTO
- [ ] Multi-AZ vs Multi-Region Deployment
- [ ] Failover Strategies

## 6. Scalability and Performance

**Priority: P0 — Very high**

- [ ] Horizontal vs Vertical Scaling
- [ ] Stateless vs Stateful Services
- [ ] CDN Architecture
- [ ] Reverse Proxy
- [ ] API Gateway
- [ ] Rate Limiting Algorithms (Token Bucket, Leaky Bucket, Sliding Window)
- [ ] Fanout on Write vs Fanout on Read
- [ ] Data Hotspots
- [ ] Pagination (Offset vs Cursor-based)
- [ ] Database Query Optimization
- [ ] Connection Pooling
- [ ] Throughput vs Latency
- [ ] Tail Latency (P95, P99)
- [ ] Capacity Estimation and Traffic Estimation

## 7. Real-Time Systems

**Priority: P1 — High**

- [ ] WebSockets
- [ ] Server-Sent Events (SSE)
- [ ] Long Polling vs Short Polling
- [ ] WebSocket Scaling
- [ ] Pub/Sub Architecture
- [ ] Real-Time Fanout
- [ ] Presence Management
- [ ] Heartbeat and Connection Management
- [ ] WebSocket Connection Routing
- [ ] Handling Millions of Concurrent Connections

## 8. Search and Data Processing

**Priority: P1 — High**

- [ ] Inverted Index
- [ ] Elasticsearch Architecture
- [ ] Full-Text Search
- [ ] Geospatial Indexing (Geohash, QuadTree, R-Tree)
- [ ] Batch Processing vs Stream Processing
- [ ] Apache Spark Fundamentals
- [ ] Apache Flink Fundamentals
- [ ] OLTP vs OLAP
- [ ] Data Aggregation
- [ ] Materialized Views

## 9. Security and API Design

**Priority: P1 — High**

- [ ] Authentication vs Authorization
- [ ] OAuth 2.0
- [ ] JWT
- [ ] API Key Authentication
- [ ] TLS / HTTPS
- [ ] Encryption at Rest vs In Transit
- [ ] Secrets Management
- [ ] API Versioning
- [ ] Idempotency Keys
- [ ] Request Validation
- [ ] Pagination and Filtering
- [ ] Audit Logging

## 10. Observability and Monitoring

**Priority: P1 — High**

- [ ] Logs, Metrics and Distributed Tracing
- [ ] Correlation IDs
- [ ] OpenTelemetry Fundamentals
- [ ] RED Metrics (Rate, Errors, Duration)
- [ ] USE Metrics (Utilization, Saturation, Errors)
- [ ] SLI, SLO and SLA
- [ ] Alerting and Monitoring
- [ ] Distributed Request Tracing
- [ ] Error Budgets

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
