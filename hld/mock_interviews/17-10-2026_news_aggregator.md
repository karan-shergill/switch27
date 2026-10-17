# News Aggregator

# 17th Oct 2026

https://www.hellointerview.com/practice/system-design/cmvasfe890v8w01adebes80yz

![17-10-2026_news_aggregator](../images/17-10-2026_news_aggregator.png)

## Feedback

1. **Requirements**
    1. In news feed systems, always favor availability over consistency. Users seeing a feed that is 1-2 seconds stale is far better than the feed failing to load. This is a classic AP choice in the CAP theorem.
    2. Set concrete latency targets early in your design. For feed-loading systems, under 200ms is a strong benchmark because research shows users perceive delays above 200ms as sluggish, which directly impacts engagement.
    3. Always anchor your design with scale numbers like DAU and spike multipliers. For example, 100M DAU with 3x traffic spikes during breaking news helps you size caches, queues, and servers with real justification rather than guessing.
2. **Core Entities**
    1. In a news aggregator system, the three core entities are User, Publisher, and News Article. User is easy to forget because the focus is often on content, but without a User entity you cannot support personalization, subscriptions, or saved articles.
3. **API**
    1. RESTful API endpoints should be named after the resource being returned, not the feature. Use nouns like /feed or /articles instead of /newsfeed. Think of it as naming what the server gives back, not what the UI calls it.
    2. Cursor-based pagination is preferred over offset-based pagination for infinite scroll feeds because new items can be inserted in real time. Offset pagination breaks when new rows shift positions, causing duplicate or skipped items. A cursor anchors to a specific record instead.
4. **High Level Design**
    1. When designing a news ingestion system that polls thousands of sources, store publisher feed metadata explicitly alongside article records. This means keeping fields like feed URL and last polled timestamp in a publishers table so your collector knows when to re-fetch each source and can track thousands of feeds over time without losing state.
    2. RSS feeds are a pull model, not a push model. Your collector polls the RSS endpoint on a schedule and fetches new items. This is simpler to design around than a subscription or webhook model because you control the timing and do not need to manage incoming connections from publishers.
    3. In a regional news feed, the serving path should explicitly resolve the user region first before hitting the cache or database. The flow is: get user region from the request or user profile, then query the regional cache using that region as a filter, then fall back to the database if the cache misses. Making this step explicit in your design shows the interviewer you understand how the region attribute actually drives the query.
5. **Deep Dives**
    1. Publisher capability tiers (RSS, webhook, scrape-only) should not be static. You need a periodic health-check or audit job that re-validates each publisher's capabilities and tracks failure rates, for example counting consecutive missed webhooks, so you can automatically promote or demote publishers between tiers as the real world changes. Without this, your classification table goes stale and freshness guarantees break.
    2. When serving images in a news feed, always generate multiple thumbnail sizes at upload time for different devices and screen densities. Sending a full desktop-resolution image to a mobile client wastes bandwidth and increases load time. Store each size variant in object storage and serve the right one via CDN based on the client's device type.
    3. For cursor-based pagination on a feed ordered by article ID, the query pattern is: fetch items WHERE id is less than the last seen ID, ORDER BY id descending, LIMIT page size. This makes the database query a simple range scan and avoids page drift when new content is inserted at the top while a user is scrolling.
    4. When using a bounded Redis cache for a regional feed, keep a fixed count of the most recent articles per region and evict the oldest when a new one arrives. For users who scroll past that window, fall back to the database to serve older articles. This bridges the gap between a fixed-size cache and an infinite scroll experience without letting the cache grow unbounded.
