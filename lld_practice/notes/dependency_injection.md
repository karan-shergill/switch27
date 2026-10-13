# Dependency Injection in Plain Java

> These notes explain Dependency Injection (DI) using plain Java — no Spring, annotations, or frameworks.

## 1. What problem does Dependency Injection solve?

Imagine you're building a notification system. An `EmailService` sends emails, and a `NotificationService` uses it.

### Approach 1: Create the dependency inside the class

```java
class EmailService {
    public void sendEmail(String message) {
        System.out.println("Email sent: " + message);
    }
}

class NotificationService {
    private EmailService emailService = new EmailService();

    public void notifyUser(String message) {
        emailService.sendEmail(message);
    }
}
```

Usage:

```java
NotificationService service = new NotificationService();
service.notifyUser("Order placed!");
```

This works, but `NotificationService` is responsible for creating its own `EmailService`. That creates tight coupling.

If you want to switch to SMS or use a fake service for unit testing, you need to modify `NotificationService` or find another way to replace its internally created dependency.

**Core problem:** the class controls the creation of a dependency that it uses.

## 2. What is Dependency Injection?

Dependency Injection means:

**Instead of a class creating the object it needs, someone else provides that object to it.**

### Approach 2: Inject the dependency through a constructor

```java
class EmailService {
    public void sendEmail(String message) {
        System.out.println("Email sent: " + message);
    }
}

class NotificationService {
    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void notifyUser(String message) {
        emailService.sendEmail(message);
    }
}
```

Calling code:

```java
public class Main {
    public static void main(String[] args) {
        EmailService emailService = new EmailService();

        NotificationService notificationService =
                new NotificationService(emailService);

        notificationService.notifyUser("Order placed!");
    }
}
```

Notice the difference:

- `NotificationService` no longer creates `EmailService`.
- The caller creates the dependency and passes it into the constructor.
- `NotificationService` simply uses the object it receives.

**That is Dependency Injection. No Spring is required.**

### Before DI vs. after DI

- **Before DI:** `NotificationService` creates `EmailService` internally.
- **After DI:** the calling code creates `EmailService` and passes it to `NotificationService`.

## 3. Types of Dependency Injection

There are three commonly discussed ways to inject a dependency in plain Java.

### A. Constructor injection

Pass the dependency through the constructor.

```java
class PaymentService {
    public void pay() {
        System.out.println("Payment processed");
    }
}

class OrderService {
    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.pay();
    }
}
```

Usage:

```java
PaymentService payment = new PaymentService();
OrderService order = new OrderService(payment);

order.placeOrder();
```

**Why prefer it?**

- Dependencies are available when the object is constructed.
- Dependency fields can be declared `final`.
- The object cannot be created without providing its required dependencies.

Use it for dependencies a class must have to work correctly.

### B. Setter injection

Provide the dependency through a setter after creating the object.

```java
class OrderService {
    private PaymentService paymentService;

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        if (paymentService == null) {
            throw new IllegalStateException("PaymentService not configured");
        }

        paymentService.pay();
    }
}
```

Usage:

```java
OrderService order = new OrderService();
order.setPaymentService(new PaymentService());

order.placeOrder();
```

**When is it useful?** When a dependency is optional or intentionally configurable after construction.

**Drawback:** someone might forget to call the setter, leaving the object incompletely configured.

### C. Method injection

Pass the dependency directly to the method that needs it instead of storing it in a field.

```java
class PaymentService {
    public void pay() {
        System.out.println("Payment processed");
    }
}

class OrderService {
    public void placeOrder(PaymentService paymentService) {
        paymentService.pay();
    }
}
```

Usage:

```java
OrderService order = new OrderService();
order.placeOrder(new PaymentService());
```

**When is it useful?** When a dependency is needed only for one operation.

For example, a method might receive a formatter, validator, or callback used only for that invocation.

### Quick comparison

| Type | How the dependency is supplied | Typical use |
|---|---|---|
| Constructor | Constructor parameter | Required dependencies |
| Setter | Setter method | Optional or configurable dependencies |
| Method | Method parameter | Needed for one operation |

**Interview takeaway:** Constructor injection is generally the best default for required dependencies.

## 4. When is Dependency Injection not needed?

DI is not a rule that every Java object must follow.

Consider a simple, self-contained calculator:

```java
class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

Usage:

```java
Calculator calculator = new Calculator();
int result = calculator.add(10, 20);
```

There's no need to inject anything because `Calculator` has no external object dependency.

DI is often unnecessary when:

- A class is simple and self-contained.
- An object is cheap to create and has no collaborators.
- A method only needs primitive or immutable values, such as `int`, `String`, or `LocalDate`.
- Introducing injection would add more complexity than flexibility.

For example, don't build an elaborate dependency injection setup just to supply two integers to an addition method.

**Important distinction:** creating an object with `new` is not inherently bad. The concern is whether a class creates and controls dependencies that you may want to replace or configure independently.

## 5. When should you use Dependency Injection?

Use DI when a class depends on another object whose implementation, configuration, or lifecycle you may want to control from outside.

Consider an order service that needs a payment processor.

### Step 1: Define an interface

```java
interface PaymentProcessor {
    void pay(double amount);
}
```

### Step 2: Create multiple implementations

```java
class CreditCardProcessor implements PaymentProcessor {
    public void pay(double amount) {
        System.out.println("Paid using credit card: " + amount);
    }
}

class UpiProcessor implements PaymentProcessor {
    public void pay(double amount) {
        System.out.println("Paid using UPI: " + amount);
    }
}
```

### Step 3: Inject the dependency

```java
class OrderService {
    private final PaymentProcessor paymentProcessor;

    public OrderService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void placeOrder(double amount) {
        paymentProcessor.pay(amount);
    }
}
```

### Step 4: Choose the implementation outside the class

```java
public class Main {
    public static void main(String[] args) {
        PaymentProcessor processor = new UpiProcessor();

        OrderService orderService = new OrderService(processor);
        orderService.placeOrder(1000);
    }
}
```

Output:

```text
Paid using UPI: 1000.0
```

To switch to credit card payments:

```java
PaymentProcessor processor = new CreditCardProcessor();

OrderService orderService = new OrderService(processor);
orderService.placeOrder(1000);
```

The implementation changed without modifying `OrderService`.

### Why this design is better

- **Loose coupling:** `OrderService` depends on the `PaymentProcessor` interface rather than a particular implementation.
- **Easy testing:** inject a fake payment processor and test order logic without making a real payment.
- **Easy extension:** add another payment implementation without changing the order service.

**Important:** DI and interfaces are not the same thing. You can inject a concrete class, as in the `EmailService` example. Interfaces simply make it easier to substitute implementations.

## 6. Dependency Injection vs. Inversion of Control (IoC)

They are related, but not identical.

- **Dependency Injection (DI):** an object receives its dependencies from outside instead of creating them itself.
- **Inversion of Control (IoC):** a broader principle in which control over some responsibility is moved away from a component to another component or framework.

In the plain Java examples here, `Main` creates the objects and wires them together. This is manual dependency injection; no framework or container is needed.

## 7. Interview cheat sheet

| Interview question | Short answer |
|---|---|
| What is DI? | Providing an object with its dependencies from outside rather than having it create them itself. |
| What problem does it solve? | Reduces coupling and makes implementations easier to replace and tests easier to write. |
| What are the types? | Constructor, setter, and method injection. |
| Which type is preferred? | Constructor injection for required dependencies. |
| Do we need DI everywhere? | No. Simple, self-contained classes generally don't need it. |
| Does DI require Spring? | No. It can be implemented entirely in plain Java. |
| Who creates the objects? | In manual DI, the calling code or a composition root creates and wires them. |
| Does DI always require an interface? | No, but interfaces are useful when multiple implementations are possible. |

## The one thing to remember

**Dependency Injection is fundamentally about who supplies the dependency. The caller supplies it; the class uses it.**

For Java interviews, explain the `NotificationService` example first, then show how constructor injection plus an interface lets you switch implementations and write unit tests more easily.
