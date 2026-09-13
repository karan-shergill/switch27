# Instagram News Feed - 12 Sep 2026

https://www.hellointerview.com/practice/system-design/cmtxqnb530ogn08adzryi2ahg

![12-09-2026_intagram_news_feed](images/12-09-2026_intagram_news_feed.png)

# Feedback 

1. **Requirements**
    1. Feed latency and media delivery latency are two distinct requirements in a content platform like Instagram. 
    2. When stating latency requirements in a system design interview, always attach a concrete number such as under 500ms for feed load time.
2. **Core Entities**
    1. Always separate a Media entity from a Post entity in photo or video platforms. The Post holds metadata like captions, timestamps, and user IDs, while Media represents the actual file bytes stored in an object store like S3.
    2. A Follow relationship should be modeled as its own entity, not just a field on a User. A Follow record links two user IDs directionally (follower_id and followee_id), making it easy to query follower counts, build feeds, and handle follow or unfollow actions efficiently.
3. **API**
    1. Take care of things that need to go as query param and as request body
    2. Remember to use Cursor-based pagination
    3. When identifying users in API requests, prefer a stable user ID over a username. Usernames can change over time, which would break any stored references or relationships. User IDs are immutable and safer to use as identifiers across your system.
4. **High Level Design**
    1. In a follow graph, model each follow relationship as a single directed edge row with two explicit fields: follower_user_id and followee_user_id. Add a secondary index on followee_user_id so you can efficiently look up followers in reverse. This keeps the graph queryable in both directions without duplicating rows.
    2. When modeling a post creation flow, your post table should include a status field (like pending or published) and a created_at timestamp. The status field lets you track whether the upload has completed before surfacing the post, and created_at is what makes chronological sorting possible later in the feed.
    3. Know the tradeoff between push-based and pull-based feed generation. Push (fan-out on write) precomputes feeds so reads are fast, but it is expensive for users with millions of followers. Pull (fan-out on read) aggregates the feed at read time, which is simpler but slower. Being able to name this tradeoff shows you understand when each approach breaks down.
5. **Deep Dives**
    1. For social feed scaling, use a hybrid fan-out strategy. Regular users get fan-out on write, meaning posts are precomputed into each follower's feed table at write time for fast reads. Celebrity users (above a follower threshold like 100,000) use fan-out on read, meaning their posts are fetched and merged at request time. This avoids write amplification when a celebrity posts to millions of followers.
    2. When merging two paginated sources (like a precomputed feed table and a celebrity posts table), never fetch all records just to paginate. Instead fetch a slightly larger window than your page size from each source, for example 25 from each, merge and sort them, then return the top 20. This bounds the work per request regardless of total feed size.
    3. When paginating across two independent data sources, your cursor must encode the last seen position from both sources independently, for example the last post ID or timestamp from each list. A single cursor value is not enough because each source needs to know where to resume on the next page fetch.
