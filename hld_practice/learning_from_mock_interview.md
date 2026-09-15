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
