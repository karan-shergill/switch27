# Web Crawler

# 7th Oct 2026

## Feedback

https://www.hellointerview.com/practice/system-design/cmuy0udup1y4r01ad6muezl6z

![7-10-2026_web_crawler](images/7-10-2026_web_crawler.png)

1. **Requirements**
    1. Fault tolerance is a must-have requirement for any long-running distributed system like a web crawler. At scale, crashes and network failures are guaranteed, so your design needs checkpointing or a persistent queue so the crawler can resume from where it left off rather than starting over or re-crawling pages.
    2. Always attach a time window to your scale targets. Saying 'crawl 10B pages within 3 weeks' is more useful than just '10B pages' because it lets you calculate the throughput you need, for example pages per second, which drives decisions about how many crawler workers to run.
2. **Core Entities**
    1. For a web crawler, the two core entities are Seed URLs (the starting addresses the crawler uses to discover pages) and Webpages (the actual content extracted and stored as output). Seed URLs are inputs, Webpages are outputs, and everything else in the system exists to move between these two.
3. **System Interface**
    1. A web crawler starts with seed URLs, which are the initial set of URLs used to begin discovering and fetching pages. From each fetched page, new URLs are extracted and added to a queue, allowing the crawler to expand its reach across the web.
    2. The output of a web crawler is typically stored in a data lake rather than a structured database because the data is raw, unstructured, and high volume. Data lakes are ideal here since they store text, images, and metadata cheaply and allow downstream systems like search indexers or LLM training pipelines to process the data in their own way.
    3. Crawl metadata (such as timestamps, HTTP status codes, and source URLs) is important to store alongside raw content because it helps downstream systems filter stale or broken pages and supports re-crawl scheduling logic.
4. **Data Flow**
    1. A web crawler pipeline has a specific order of steps after fetching a page: fetch raw HTML, parse the HTML to extract plain text, store the text for downstream consumers like search indexers, and separately extract links to feed back into the URL frontier. Skipping the parsing step means indexers receive raw HTML tags instead of clean content, which breaks search quality.
    2. The core crawl loop has four main stages: pull URL from frontier queue, fetch the page, process and store the content, push newly discovered URLs back into the frontier. Keep this loop clear in your answer before adding details like DNS caching or rate limiting, so interviewers can follow the flow easily.
5. **High Level Design**
    1. A web crawler needs an explicit stopping condition or it runs forever. Common stopping conditions are a max page count limit, an empty frontier queue, or a configured domain boundary. Always state one of these in your design so the interviewer knows the system terminates.
    2. The parser in a crawler does two jobs, not one. It extracts the page text for storage AND extracts all outgoing links to feed back into the frontier queue. Make this loop explicit by saying 'the parser outputs text to storage and new URLs to the queue' so the interviewer can clearly see the fetch, extract, enqueue cycle.
    3. When adding operational components like a dead letter queue, bloom filter, or retry logic, connect each one back to the main flow before explaining it. For example, say 'the bloom filter sits in front of the queue to prevent re-crawling already visited URLs' rather than listing it as a standalone piece. This keeps the baseline design readable.
6. **Deep Dives**
    1. When classifying crawler failures, always name all four categories out loud: timeouts and connection errors (transient, retry with backoff), 5xx server errors (transient, retry with backoff and capped attempts), 429 rate limiting (transient, retry with backoff and honor the Retry-After header if present), and 4xx client errors like 404 or 410 (permanent, send straight to the DLQ). Missing any one of these leaves your failure handling incomplete. ⭐️
    2. A Bloom filter has false positives, meaning it will sometimes say a page is a duplicate when it is actually new, causing you to skip crawling it. The right pattern is to use the Bloom filter as a fast precheck only. If the Bloom filter says 'definitely not seen', trust it and crawl. If it says 'maybe seen', confirm against your exact page hash store before skipping. The exact store is always the source of truth.
    3. Saying 'multiple workers pull from the queue in parallel' is not enough to explain distributed crawling. You also need to explain how work gets assigned without contention. A common approach is to partition the URL queue by domain so each worker owns a set of domains, which prevents multiple workers from hammering the same host and avoids load skew from large domains. ⭐️
    4. When applying exponential backoff to 5xx errors, make sure to say explicitly that the retry uses a delay, not an immediate re-queue. A failing server hit again right away will likely fail again. Use queue visibility delay or a scheduled retry so the server has time to recover, and cap the total retry count before sending to the DLQ.
