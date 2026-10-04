# Deep-dive Qs to revise

1. YouTube
    1. Users may have poor or fluctuating network connections. How would you design your system to ensure that video streaming continues smoothly under these conditions?
    2. How would your client decide when to switch up or down in quality so it avoids frequent oscillation between 720p and 1080p when bandwidth is hovering near the cutoff?
    3. Uploading large video files can be challenging due to network interruptions. Explain how your design allows users to resume an interrupted upload without starting over from scratch.
    4. Suppose the user loses connectivity halfway through and comes back an hour later from a fresh browser session. How would your client use the stored upload state to figure out exactly which chunks still need to be sent?
    5. How would you design your system to allow users to resume watching a video from where they left off, even if they switch devices?
2. Instagram Feed
    1. How would you scale the feed generation to support users who follow thousands of accounts while maintaining low latency?
    2. How would you keep the merged feed in strict chronological order and paginated correctly when one part comes from a precomputed feed table and the other part is fetched on read from celebrity accounts
    3. How would you handle the upload of large media files efficiently, particularly videos that could be up to 4GB in size?
    4. How would you ensure fast media delivery to users globally, with photos and videos rendering quickly regardless of a user's location?
3. BookMyShow
    1. Implement a two-phase booking process: 1. Seat Reservation: Temporarily hold selected seats. 2. Booking Confirmation: Finalize purchase within a time limit. How would you design this to prevent users from losing seats during checkout?
    2. How would you make the hold operation safe if two users try to reserve the same seat at nearly the same time and both booking requests hit your Booking Service together?
    3. How can your design scale to support up to 10M concurrent users reading event data? Focus on optimizing the database and read flow for this high volume of requests.
    4. How can you make the seat map on the event page automatically refresh to display the latest seat availability in real time?
4. **WhatsApp**
    1. How 1-to-1 and group chat will work, send and receive messages
    2. Design to allow users to receive messages later if their client is offline
    3. How can we enable billions of simultaneous users?
    4. What do we do to handle multiple clients for a given user?
5. LeetCode
    1. How will users be able to view a live leaderboard for competitions?
    2. List off some ways that you'd support isolation and security when running user code?
    3. How would you enforce that isolation so the worker can still send code in and collect results, but the user program itself cannot initiate outbound communication?
    4. How would the system scale to support spikes of submissions during competitions while not dropping requests?
    5. How would you decide when to scale up or scale down the worker and sandbox capacity from queue signals, while avoiding thrashing during a bursty contest ending?
6. Distributed Rate Limiter
    1. What is the interface of your rate limiting component? Describe the inputs it needs (e.g., client ID, rule info) and outputs it returns (e.g., allow/deny decision, remaining quota).
    2. Where should the rate limiter be placed in your overall system? Consider the trade-offs of different placement options.
    3. What rate limiting algorithm would you use and why?
    4. How do we scale to handle 1M requests/second?
    5. How would you handle rebalancing when you add or remove Redis shards so that clients do not see inconsistent rate limit decisions during the transition?
    6. What happens when our rate limiting system fails? Should we pass all traffic or block all traffic, and how do we recover quickly?
    7. You said you would handle the outage gracefully rather than allow or deny everything. How would your gateway decide how much traffic to allow for a given client while Redis is down if it cannot read the current counters?
    8. How do we minimize latency overhead?
    9. How do we handle dynamic rule configuration? Where are rules stored and how does our gateway know about them?
7. Distributed Cache Like Redis
    1. Design a basic, single-node cache that supports get, set, and delete operations.
    2. How would you implement TTL (time-to-live) functionality for cache entries?
    3. How would you expand on your design to include an LRU eviction policy?
    4. How would you ensure your cache is both highly available and fault tolerant?
    5. How would you decide whether replicas are allowed to serve reads immediately after a write, and what tradeoff would that create for clients that expect fresh values?
    6. How would you ensure your cache can scale dynamically to support large amounts of data up to 1TB?
    7. How would you handle requests during the period when keys are being remapped after adding a new cache node so that clients do not see inconsistent reads or a large spike in cache misses?
    8. What if one key is extremely hot? How Read & Write will be handled for that key?
8. Notification System
    1. How does your system guarantee that an accepted notification is never dropped, even if a worker crashes or a provider has an outage?
    2. How would you make your write to storage and your enqueue to the next stage behave as one durable handoff so a crash between those two actions cannot strand accepted work?
    3. How would you ensure critical notifications like OTPs and account alerts are delivered within 5 seconds, even while a million-user campaign is being delivered?
    4. Your system guarantees at-least-once delivery. How do you prevent a user from receiving the same OTP or promotional message multiple times when retries and worker restarts happen?
    5. If one SMS or email provider becomes slow or starts failing, how do you stop that provider from stalling delivery for the rest of the system?
9. Job Scheduler
    1. How can we ensure the system executes jobs within 2s of their scheduled time? If your design already does, explain how.
    2. How would your design handle a job that gets created at 5 02 for execution at 5 03 after the 5 00 scan has already finished?
    3. How can we scale job execution to support up to 10,000 concurrent jobs executing in parallel?
    4. You mentioned scaling workers from SQS backlog. How would you choose the signals and thresholds so the worker fleet scales up fast enough for bursts without overreacting to short queue spikes?
    5. How should we handle retrying jobs that fail during execution?
    6. What will happen if a worker node running a job, died in the middle of execution?
    7. What if we are not allowed to use SQS, Kafka doesn't have visibility timeout. How do you handle worker failure?
10. Dropbox
    1. How will users be able to upload files? How will users be able to download files from remote storage?
    2. Design how the Dropbox desktop / mobile sync agent detects edits in the user's local Dropbox folder and uploads those changes to remote storage.
    3. Design how the sync agent on a device discovers changes that happened in the cloud and applies them to the local file system.
    4. How will your system handle uploading large files (up to 50GB) given the limitations of most servers and clients on the size of a POST request body?
    5. Uploading large files can also be challenging due to network interruptions. How does your design allow users to resume an interrupted upload without starting over from scratch?
    6. How would you prevent a client from finalizing an upload if some chunks were duplicated, missing, or uploaded out of order after several retries and parallel uploads?
    7. How can we reduce bandwidth usage and make the sync process faster than downloading full files each time they change?
11. Uber
    1. How would you give users a estimated fare based on their start location and destination?
    2. How will riders be able to request a ride based on the estimated fare?
    3. How does your system match riders to the best driver for their ride?
    4. How does your system notify matched drivers and allow them to accept/decline rides?
    5. How can you handle the high write throughput from drivers sending location updates every couple seconds and efficiently perform proximity searches for matching?
    6. How do we guarantee each driver receives at most one ride request at a time?
    7. How can we ensure no ride requests are dropped during peak demand periods?
    8. How would you handle a trip message that has been picked by one Request Driver Service worker, but that worker crashes after sending some driver notifications and before finishing the trip update?
