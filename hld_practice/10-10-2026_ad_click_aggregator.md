# Ad Click Aggregator

# 10th Oct 2026

https://www.hellointerview.com/practice/system-design/cmmah8fnv0qfd07adcjdmwyvr

![10-10-2026_ad_click_aggregator](images/10-10-2026_ad_click_aggregator.png)

## Feedback

1. **Requirements**
    1. For ad click tracking systems, the redirect path (the moment a user clicks an ad) must be extremely fast, typically under 100ms, because any slowdown directly hurts user experience and advertiser trust. Design this path to be lightweight and separate from heavier analytics processing.
    2. Duplicate click handling is critical in click tracking because network retries and bot traffic can inflate counts. A common pattern is to use idempotency keys (like a hash of user ID plus ad ID plus time window) to deduplicate clicks before they reach your aggregation layer.
    3. Eventual consistency is an acceptable tradeoff for advertiser dashboards because advertisers viewing their metrics a few seconds or minutes delayed is fine. This lets you batch and aggregate click data asynchronously, which is much cheaper and more scalable than trying to show real-time counts.
2. **Core Entities**
    1. In an ad click aggregator, you need two separate entities: a raw ClickEvent (stores each individual click as it happens) and a ClickCount (the rolled-up aggregate). The ClickEvent is the input to your system, and ClickCount is the output. Without raw events, you have nothing to aggregate from.
    2. When designing any metrics or analytics system, think in two layers: the raw event layer (what gets recorded in real time) and the aggregation layer (what gets computed from those raw events). Always start by modeling the raw event entity first, then figure out how you roll it up.
3. **System Interface**
    1. In a system design, the input is the raw data the system receives from the outside world. For an ad click tracking system, the input is the click event itself (containing data like ad ID, user ID, timestamp) because that is what triggers everything else. Ad configuration data like redirect URLs is setup data, not the live input the system processes.
    2. When defining system boundaries, ask yourself what event causes the system to do work. That event is your input. Everything the system produces from that event is your output. This separation makes it clear what the system is responsible for and what it depends on.
4. **Data Flow**
    1. When designing an ad click system, always include the redirect step where clicking an ad first hits your tracking server to record the event, then redirects the user to the advertiser's landing page. This is how real ad systems like Google Ads work and shows you understand both the user experience and the data capture needs at the same time.
5. **High Level Design**
    1. When designing a click tracking system, always state your event schema explicitly. A minimal click event should include ad_id, timestamp, user or device identifier, and a reference to the destination URL or campaign. This makes it clear what gets stored and how it gets aggregated later.
    2. For ad analytics reporting, name the specific queries your OLAP store needs to support, such as total clicks per ad_id grouped by time range. This shows you understand the read access pattern that drives your storage choice, not just that OLAP exists. ⭐️
    3. Structure your system design answer in two layers. First describe the simplest working path, for example a redirect service that logs a click event and sends the user to the destination. Then mention enhancements like reconciliation jobs as follow-on improvements. This keeps your answer sharp and easy to follow.
6. **Deep Dives**
    1. Use Redis SET with NX and EX together in a single atomic command for deduplication. If you do a separate 'check then set' in two steps, two concurrent requests can both pass the check and both get counted. The atomic SET NX EX command only succeeds for the first caller and sets the TTL in the same operation, closing the race condition entirely. ⭐️
    2. For deduplication TTL windows, drop the epoch minute from the key and let the TTL define the window instead. A key like Hash(ad_id + user_id) with a 60 second TTL means any click within 60 seconds of the first accepted click is rejected regardless of clock minute boundaries. Using a fixed epoch minute bucket creates a gap where clicks at 12:00:59 and 12:01:01 are treated as different keys even though they are two seconds apart. ⭐️
    3. To handle hot partition keys in Kafka, combine the base key like ad_id or campaign_id with a salt such as a hash of click_id or device_id to spread one hot campaign across multiple partitions. Counts are additive, so the Flink consumers can simply sum the partial counts from all sub-partitions before writing to the OLAP store. The key insight is that partitions within one topic are the unit of parallelism, not separate topics. ⭐️
    4. For long time range analytics queries, precompute aggregates at coarser time buckets like hourly or daily and store them in separate rollup tables. Scanning minute level data for a full year query is very expensive. A good design routes short range queries to fine grained tables and long range queries to coarser precomputed summaries, with the tradeoff being that very recent data may not yet appear in the rollups.
