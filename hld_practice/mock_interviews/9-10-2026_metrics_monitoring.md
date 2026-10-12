# Metrics Monitoring

# 9th Oct 2026

https://www.hellointerview.com/practice/system-design/cmv0l0tg603f001adj2r2adbs

![9-10-2026_metrics_monitoring](../images/9-10-2026_metrics_monitoring.png)

## Feedback

1. **Requirements**
    1. For monitoring systems, always separate latency requirements into two buckets: query latency (how fast dashboards load for users) and alert latency (how fast the system fires alerts when a threshold is breached). These often have different targets and different architectural solutions.
    2. At 5M metrics per second, the ingestion path must be designed for high availability independently from the query path. A write failure during an incident is catastrophic, so ingestion pipelines often use durable message queues like Kafka to buffer writes and prevent data loss even if downstream storage is temporarily unavailable.
    3. Eventual consistency is an acceptable trade-off in monitoring systems because metric data is time-series based and append-only. Slightly stale dashboard reads are tolerable, but the ingestion path must guarantee no data loss, making fault tolerance a harder requirement than strict consistency.
2. **Core Entities**
    1. A 'Series' is the core unit of storage in a metrics monitoring system. It represents the full sequence of timestamp-value pairs for one unique combination of a metric name and its labels. For example, cpu_usage with host=server-1 is one series, and cpu_usage with host=server-2 is a completely separate series. Always include Series as a core entity in monitoring system designs.
        1. A **label** is extra information that describes where a measurement came from, such as **host=server-1** or **region=us-east**. A **metric** is the kind of measurement, such as **cpu_usage**. A **series** is all recorded values over time for one metric with one exact set of labels.
        2. For example, **cpu_usage{host=server-1}** might have **(10:00, 70%)**, **(10:01, 75%)**, and **(10:02, 72%)**. Those points are one series. **cpu_usage{host=server-2}** is a separate series because its label value is different.
    2. Label combinations cause a 'cardinality explosion' in monitoring systems. One metric name like cpu_usage can fan out into thousands or millions of individual series when you multiply across all label values (hosts, regions, services, etc.). This is the primary driver of storage and query scaling challenges in systems like Prometheus or Datadog, so understanding series cardinality is key to justifying your scaling decisions.
3. **API**
    1. Metric ingest endpoints must accept batches, not single data points. At millions of events per second, one POST per metric is impractical. The request body should be an array where each item has a name, labels, a value, and a timestamp so one request carries many data points.
    2. A time-series query endpoint needs explicit inputs to be useful. The body should include a query expression like avg(cpu_usage{region='us-east'}), a start time, an end time, and a step interval. The response should return a list of timestamp-value pairs.
    3. An alert rule needs both a threshold condition and a duration. For example, trigger when p99 latency exceeds 500ms for 5 consecutive minutes. The duration prevents noisy alerts from brief spikes and is a standard part of alerting systems like Prometheus.
    4. Use plural nouns for collection endpoints. POST /metrics and POST /alerts are correct because you are adding to a collection. This is a REST convention interviewers notice and expect.
4. **High Level Design**
    1. Always put a durable queue or outbox table between an alert evaluator and a Notification Service. If you call the Notification Service directly and it is down, the alert is silently lost. A queue like Kafka or a database table with a status column gives you guaranteed delivery and retry without losing events. ⭐️
    2. Alert systems need firing and resolved state tracking so notifications only send on transitions. Without this, every evaluation cycle that finds a condition still true will re-send the same alert, spamming users. Store the current state per rule and only notify when the state changes from ok to firing or back. ⭐️
    3. Kafka partitioning is how you scale a high throughput ingestion pipeline. At 5 million metrics per second, Kafka is not a single pipe. You partition by a key like host or metric name so that load is spread across multiple brokers and consumed in parallel by multiple consumer instances.
    4. Alert rule schema should include evaluation interval and condition duration fields. A rule like p99 latency over 500ms for 5 minutes cannot be expressed without these. The evaluator reads these fields to know how often to run and how long a condition must hold before firing.
5. **Deep Dives**
    1. When caching dashboard query results in Redis, store them as time-bucketed chunks keyed by query and resolution rather than one blob per exact time range. This way, when the time window shifts by a few minutes, you can reuse the stable older buckets and only fetch the new trailing buckets, instead of recomputing the entire range from scratch. ⭐️
    2. Using event time alone is not enough to handle late or out-of-order metrics in a streaming alert evaluator. You also need a bounded lateness window, sometimes called a grace period or watermark, that keeps the sliding window open for a short time to accept stragglers before finalizing the result. Without this, late events that would have triggered a breach get silently ignored. ⭐️
    3. For high availability in a metrics pipeline, redundancy needs to exist at every layer, not just at ingestion. This means multiple ingest instances behind a load balancer, replicated queue partitions across availability zones, a replicated time series store, multiple alert evaluators running in parallel, and local agent buffering with retries so data is not lost when the network or ingest tier goes down temporarily.
    4. An allowlist of approved label names alone is not enough to prevent cardinality explosion. You also need a hard cap on the number of distinct time series a single metric can create. Without the cap, an approved label with a large value space like a user ID or session ID can still cause an explosion. Pair the cap with an internal counter and alert so operators can see when the system is silently dropping new series.
