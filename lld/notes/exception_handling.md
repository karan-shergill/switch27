# Java Exception Handling

Short theory, practical examples, and interview-ready rules. Plain Java; no Spring.

## Table of Contents

- [1. Why handle exceptions?](#1-why-handle-exceptions)
- [2. Fail-fast vs. graceful degradation](#2-fail-fast-vs-graceful-degradation)
  - [Fail-fast: invalid payment amount](#fail-fast-invalid-payment-amount)
  - [Graceful degradation: recommendations are optional](#graceful-degradation-recommendations-are-optional)
- [3. Checked vs. unchecked exceptions](#3-checked-vs-unchecked-exceptions)
  - [Checked exception](#checked-exception)
  - [Unchecked exception](#unchecked-exception)
- [4. Custom business exceptions](#4-custom-business-exceptions)
- [5. Where should you throw and catch?](#5-where-should-you-throw-and-catch)
  - [Avoid swallowing exceptions](#avoid-swallowing-exceptions)
  - [Catch specific exceptions](#catch-specific-exceptions)
- [6. Preserve the original cause when wrapping](#6-preserve-the-original-cause-when-wrapping)
- [7. Cleanup resources](#7-cleanup-resources)
- [8. `Exception` vs. `Error`](#8-exception-vs-error)
- [9. Practical LLD example: inventory](#9-practical-lld-example-inventory)
- [10. LLD interview checklist](#10-lld-interview-checklist)
- [Remember](#remember)

## 1. Why handle exceptions?

Use exceptions to stop invalid operations and communicate failures clearly.

```java
if (availableSeats <= 0) {
    throw new IllegalStateException("No seats available");
}
```

**LLD rule:** Never report success if the operation failed. Exceptions do not automatically roll back state or make an operation atomic.

## 2. Fail-fast vs. graceful degradation

- **Fail-fast:** Stop immediately when input or state is invalid.
- **Graceful degradation:** Keep the core feature working when an optional feature fails.

### Fail-fast: invalid payment amount

```java
void pay(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
    }
    // Process payment
}
```

### Graceful degradation: recommendations are optional

```java
try {
    recommendationService.load();
} catch (RecommendationException e) {
    logger.warn("Recommendations unavailable", e);
}
// Continue showing the movie page
```

**Choose based on correctness:** fail fast for invalid state; degrade gracefully only when skipping the failed feature is safe.

## 3. Checked vs. unchecked exceptions

### Checked exception

Compiler requires the method to catch it or declare it with `throws`.

```java
void importFile() throws IOException {
    // Read a file
}
```

The caller must catch or declare `IOException`.

### Unchecked exception

No compiler requirement to catch or declare it. Usually a `RuntimeException` subclass.

```java
if (quantity <= 0) {
    throw new IllegalArgumentException("Quantity must be positive");
}
```

| | Checked | Unchecked |
|---|---|---|
| Compiler requires catch/declaration? | Yes | No |
| Example | `IOException` | `IllegalArgumentException`, `IllegalStateException` |
| Typical use | Failure the caller is expected to explicitly consider | Invalid arguments/state; many application/business failures |

**Interview tip:** Checked does not mean recoverable, and unchecked does not mean unrecoverable. Choose based on the API contract and what callers can reasonably do.

## 4. Custom business exceptions

Create a custom exception when it gives the caller a meaningful, specific failure.

```java
class SeatAlreadyBookedException extends RuntimeException {
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}

class Seat {
    private boolean booked;

    public void book() {
        if (booked) {
            throw new SeatAlreadyBookedException("Seat is already booked");
        }
        booked = true;
    }
}
```

Caller handles the business case:

```java
try {
    seat.book();
} catch (SeatAlreadyBookedException e) {
    System.out.println("Choose another seat");
}
```

Examples worth recognizing in LLD:
- `InsufficientBalanceException`
- `SeatAlreadyBookedException`
- `ProductOutOfStockException`
- `BookingNotFoundException`

Use `RuntimeException` for many application-specific exceptions unless the API has a clear reason to require callers to catch or declare them.

## 5. Where should you throw and catch?

- **Domain/entity:** enforce business invariants and reject invalid state transitions.
- **Service/use-case layer:** coordinate operations; catch only if it can recover or take meaningful action.
- **Repository/infrastructure layer:** translate low-level failures only when a clearer abstraction is useful; preserve the cause.
- **Application boundary/controller:** convert known failures into suitable responses and unexpected failures into safe generic errors.

Let an exception propagate if the current layer cannot handle it meaningfully.

### Avoid swallowing exceptions

Bad:

```java
try {
    paymentService.pay();
} catch (Exception e) {
    // Do nothing
}
```

Better: handle a known failure, or let it propagate. Never treat an unknown payment outcome as confirmed failure or success.

### Catch specific exceptions

```java
try {
    bookingService.book();
} catch (SeatAlreadyBookedException e) {
    showMessage("Choose another seat");
} catch (BookingUnavailableException e) {
    showMessage("Booking is temporarily unavailable");
}
```

Avoid catching `Exception` everywhere. A broad catch may be suitable at an application boundary if it logs/reports the failure and returns a safe response.

## 6. Preserve the original cause when wrapping

Use a higher-level exception when it adds useful context. Pass the original exception as the cause.

```java
class BookingPersistenceException extends RuntimeException {
    BookingPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

try {
    repository.save(booking);
} catch (SQLException e) {
    throw new BookingPersistenceException("Could not save booking", e);
}
```

This example assumes the repository exposes `SQLException`. Don't wrap the same exception at every layer without adding meaning.

## 7. Cleanup resources

Prefer try-with-resources for `AutoCloseable` resources:

```java
try (var reader = Files.newBufferedReader(Path.of("input.txt"))) {
    System.out.println(reader.readLine());
} catch (IOException e) {
    logger.error("Unable to read input file", e);
}
```

The reader is closed automatically, even if an exception occurs. Use `finally` for cleanup that try-with-resources doesn't cover. Don't return from `finally`; it can hide exceptions or override return values.

## 8. `Exception` vs. `Error`

- `Exception`: failures an application may handle or propagate.
- `RuntimeException`: unchecked subclass of `Exception`.
- `Error`: serious JVM/runtime problems, e.g. `OutOfMemoryError`, `StackOverflowError`.

Don't routinely catch `Error` or `Throwable` as ordinary application failures.

## 9. Practical LLD example: inventory

```java
class ProductOutOfStockException extends RuntimeException {
    ProductOutOfStockException(String message) {
        super(message);
    }
}

class Inventory {
    private int stock;

    Inventory(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = stock;
    }

    public void reserve(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (stock < quantity) {
            throw new ProductOutOfStockException("Insufficient stock");
        }

        stock -= quantity;
    }
}
```

What this demonstrates:
- Invalid input → `IllegalArgumentException`.
- Business rule failure → `ProductOutOfStockException`.
- State changes happen only after validation.

**Concurrency caveat:** this method alone is not thread-safe. In a real inventory system, simultaneous reservations need synchronization or an atomic/transactional storage operation.

## 10. LLD interview checklist

- Is this invalid input, invalid state, or an operational failure?
- Which layer can take meaningful action?
- Should the exception be checked or unchecked for this API?
- Would a specific custom exception improve clarity?
- Could catching it hide a failed payment or inconsistent state?
- Does the operation need rollback, transactionality, idempotency, or concurrency control beyond exception handling?
- Am I preserving the original cause when wrapping?

## Remember

**Throw where the failure is detected; catch where it can be handled meaningfully.**

Use exceptions to make failure explicit—not as a substitute for normal control flow, transaction management, or concurrency control.
