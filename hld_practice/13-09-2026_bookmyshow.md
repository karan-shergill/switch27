# BookMyShow - 13th Sep 2026

https://www.hellointerview.com/practice/system-design/cmtzzg65e3uyb07ad7dqap1z4

![13-09-2026_bookmyshow](images/13-09-2026_bookmyshow.png)

# Feedback to work on

1. **Requirements**
    1. Search latency is a first-class non-functional requirement in ticketing systems. Users browsing events expect results in under 200ms, and slow search causes abandonment before they even reach booking.
    2. Bursty traffic is a defining challenge for ticketing systems. When a popular event goes on sale, millions of users hit the system simultaneously. Your NFRs should explicitly state something like 'the system must handle 500k concurrent users during a high-demand event launch' to signal you understand this scaling pattern.
    3. When listing non-functional requirements, quantify them. Instead of saying 'fast search' or 'scalable,' say '200ms p99 latency for search' or 'support 500k concurrent users.' Numbers show architectural intent and give you something concrete to design toward.
2. **API**
    1. Never put user identity like userId in a request body. User identity should come from an auth header like a JWT or session token. 
    2. In REST APIs, query parameters belong on GET requests for filtering, while POST request data belongs in the request body. For example, eventId and seat should go in the POST /ticket body, not as query parameters, because query params are for filtering collections not sending resource data.
    3. POST endpoints should always return a meaningful response body.
3. **High Level Design**
    1. When designing a ticket booking flow, you need to explicitly close the full state machine. A seat should move through three states: available, on hold when selected, and booked after successful payment.
4. **Deep Dives**
    1. A queue alone does not prevent race conditions on seat booking. The real fix is an atomic conditional write at the database level, for example updating a ticket row only WHERE booking_status = 'free'. If another request already changed the row, zero rows are updated and you return a conflict. As a lightweight first layer, you can also use Redis SET NX with a short TTL on the seat key to reduce database contention before the conditional write.
    2. A waiting room for a ticket sale spike needs a durable, ordered queue backed by something like Redis Sorted Sets, where the score is the user's join timestamp. This enforces fair ordering, survives restarts, and can hold far more than a small fixed number of users. Users learn their position through polling or a persistent connection, and the system admits a controlled batch into the booking flow at a time.
  

