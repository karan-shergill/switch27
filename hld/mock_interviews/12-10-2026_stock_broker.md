# Stock Broker Application

# 12th Oct 2026

https://www.hellointerview.com/practice/system-design/cmv3t2ydk1ymz01adw8omyele

![12-10-2026_stock_broker](../images/12-10-2026_stock_broker.png)

## Feedback

1. **Requirements**
    1. Order management in a trading system requires strong consistency, not eventual consistency. If a user places or cancels an order and sees stale data, they could accidentally submit duplicate orders or act on incorrect account balances. Always favor strong consistency on any path that involves money or order state, even if it adds a small latency cost.
    2. Exchange data feeds are expensive and limited resources. You should never open one connection per user to an upstream exchange. Instead, open a small number of connections to the exchange and fan out the price updates to all your users from your own infrastructure. This is a common pattern called a fan-out or pub-sub model.
    3. When stating scalability requirements, name the specific bottleneck you expect to scale. For a trading platform, a strong answer would be something like supporting millions of concurrent users watching live prices and handling a spike in order requests at market open, rather than just saying the system should be scalable.
2. **Core Entities**
    1. For a stock trading platform like Robinhood, the three core data entities are Stock Symbols (the assets being traded), Users (account holders), and Orders (buy/sell requests). These are the foundation of the data model and should be your starting point when designing any trading system.
3. **API**
    1. An order creation request must include a side field (buy or sell) and a quantity field like numShares. Without these, the system literally cannot execute the trade. Think of it like placing a stock order on a real brokerage app - you always specify direction and amount.
    2. Avoid path collisions by keeping resources at the top level. GET /stocks/orders can conflict with GET /stocks/{symbol} because a router may treat 'orders' as a symbol value. Use GET /orders and POST /orders as separate top-level routes instead, and pass the stock symbol in the request body.
    3. A 202 status code means Accepted, not OK. It signals that the request was received and will be processed asynchronously. This is the right choice for order creation since trade execution happens in the background after the API responds.
    4. Keep user-facing API endpoints separate from internal system concerns. An endpoint like GET /exchange/stocks describes how your backend ingests data from a third party, not something a client would ever call. Save those internal flows for the high-level design discussion.
4. **High Level Design**
    1. When describing a read path in an interview, always state it as a full chain: client -> API gateway -> service -> database/cache -> response. This makes your design immediately clear and shows you understand how data actually flows, not just where it lives.
    2. An order record should be self-contained with fields like user_id, symbol, quantity, side (buy/sell), order_type (market/limit), status, price, and timestamps all in one table. This avoids joins for common queries and makes the data model easy to reason about.
    3. When querying orders by user, the primary key is typically order_id but the access pattern is by user_id. You should explicitly call out that user_id needs an index or be used as a partition key so the database can efficiently return all orders for one user without a full table scan.
    4. In system design interviews, start with the simplest working version before adding streaming layers like Kafka or Redis Pub/Sub. Introduce those components only when you can justify them with a specific scaling or decoupling need, otherwise the design looks over-engineered before the problem is fully established.
5. **Deep Dives**
    1. SSE connections are long-lived and stateful, so your load balancer must support them and use sticky sessions (session affinity) to pin each user to the same backend instance. Without this, reconnects may land on a different server that has no knowledge of that user's stream, breaking stream ownership. ⭐️
    2. Redis pub/sub is fire-and-forget, meaning any messages published while a client is disconnected are permanently lost. To handle reconnects correctly, the client sends its last received event ID using the SSE Last-Event-ID header, the server backfills the gap from a time series database, and then switches to live updates. Each event must have a monotonically increasing ID or timestamp so the client can discard duplicates at the handoff point. ⭐️
    3. When scaling a live price streaming tier, each server should only subscribe to the Redis pub/sub channels for symbols that its connected users actually care about. This avoids broadcasting every symbol update to every server and aligns compute with real demand. For hot symbols with many subscribers, route new connections toward servers already handling that symbol to avoid one server becoming a bottleneck.
    4. Order submission needs an explicit lifecycle with states like pending, submitted, filled, and failed. The correct pattern is to persist the order with a status of pending first, then submit it to the exchange. If the service crashes between those two steps, the order record still exists and its status makes it clear the submission may not have happened, so you can retry or alert the user rather than losing the order silently.
