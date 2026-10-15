# 4 Pillars of OOP — LLD Revision Notes

## 1. Encapsulation — Protect the Data

**Meaning:** Keep data private and expose controlled operations that enforce business rules.

**Remember:** Protect the object's state.

### Example: Bank Account

```java
class BankAccount {
    private double balance;

    public BankAccount(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Negative balance");
        }
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            throw new IllegalArgumentException("Invalid withdrawal");
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}
```

```java
BankAccount account = new BankAccount(10000);
account.withdraw(2000); // Balance = 8000
```

- `private` prevents direct modification.
- Business methods validate changes.
- Don't automatically add setters for every field.
- **LLD use:** Protect order status, account balance, inventory, and seat-booking state.

**Interview trap:** Private fields plus unrestricted setters do not necessarily provide meaningful encapsulation.

---

## 2. Abstraction — Hide Complexity

**Meaning:** Expose *what* a component can do while hiding *how* it does it.

**Remember:** Show the contract, hide the implementation.

### Example: Payment System

```java
interface PaymentProcessor {
    void pay(double amount);
}

class UpiPaymentProcessor implements PaymentProcessor {
    public void pay(double amount) {
        System.out.println("Processing UPI payment: " + amount);
    }
}

class CardPaymentProcessor implements PaymentProcessor {
    public void pay(double amount) {
        System.out.println("Processing card payment: " + amount);
    }
}
```

The checkout service only needs the contract:

```java
class CheckoutService {
    private final PaymentProcessor processor;

    CheckoutService(PaymentProcessor processor) {
        this.processor = processor;
    }

    void checkout(double amount) {
        processor.pay(amount);
    }
}
```

- The checkout service doesn't need provider-specific details.
- Java commonly uses **interfaces** and **abstract classes** for abstraction.
- **LLD use:** Hide payment, notification, storage, or external API details.

**Encapsulation vs abstraction:**
- Encapsulation: controls access to internal state.
- Abstraction: hides implementation complexity behind a contract.

---

## 3. Inheritance — Reuse and Specialize

**Meaning:** A child class derives from a parent class and can reuse or specialize its accessible behavior.

**Remember:** A *Car is a Vehicle*.

### Example: Vehicle

```java
abstract class Vehicle {
    private final String registrationNumber;

    Vehicle(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public abstract void park();
}

class Car extends Vehicle {
    Car(String registrationNumber) {
        super(registrationNumber);
    }

    @Override
    public void park() {
        System.out.println("Park in a car spot");
    }
}

class Bike extends Vehicle {
    Bike(String registrationNumber) {
        super(registrationNumber);
    }

    @Override
    public void park() {
        System.out.println("Park in a bike spot");
    }
}
```

- Use inheritance for a genuine, stable **is-a** relationship.
- `extends` is used for class inheritance.
- **Avoid:** Deep or complicated hierarchies just to reuse code.
- **Composition is often better for has-a relationships:** `Car has an Engine`.

**LLD rule:** Prefer inheritance for true subtypes; consider composition when behavior varies independently.

---

## 4. Polymorphism — Same Call, Different Behavior

**Meaning:** Code uses a common type, while the actual object's implementation determines the behavior.

**Remember:** Same call, different implementation.

### Example: Notification System

```java
interface NotificationSender {
    void send(String message);
}

class EmailSender implements NotificationSender {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SmsSender implements NotificationSender {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

class NotificationService {
    void notify(NotificationSender sender, String message) {
        sender.send(message);
    }
}
```

```java
NotificationService service = new NotificationService();

service.notify(new EmailSender(), "Order shipped");
service.notify(new SmsSender(), "Order shipped");
```

The service makes the same call, `sender.send()`, but the implementation differs.

- **Runtime polymorphism:** Method overriding.
- **Compile-time polymorphism:** Method overloading.
- Reduces repeated `if-else` checks for each implementation.
- **LLD use:** Payment methods, notification channels, pricing strategies.

---

## How the 4 Pillars Work Together

Example: A payment system

| Pillar | Application |
|---|---|
| Encapsulation | Keep transaction state controlled and validate state changes. |
| Abstraction | Define a `PaymentProcessor` interface. |
| Inheritance | Use a shared parent class only if the relationship and shared behavior justify it. |
| Polymorphism | UPI and card processors implement `pay()` differently. |

**Note:** You don't need all four pillars in every class. Choose the principle that solves the actual design problem.

## Quick Interview Recall

- **Encapsulation:** How do I protect state and enforce rules?
- **Abstraction:** What contract should callers see?
- **Inheritance:** Is this genuinely an *is-a* relationship?
- **Polymorphism:** Can different implementations respond to the same call?

## Common Interview Distinctions

- **Encapsulation vs Abstraction:** State protection vs implementation hiding.
- **Inheritance vs Polymorphism:** Class relationship/code reuse vs interchangeable behavior.
- **Interface vs Inheritance:** An interface defines a contract; class inheritance can also share implementation.
- **Composition vs Inheritance:** *Has-a* vs *is-a* (as a rule of thumb).

## LLD Design Checklist

When solving a problem, ask:
1. Which object owns this data, and what rules must it enforce?
2. What implementation details should callers not need to know?
3. Is a genuine parent-child relationship present?
4. Does behavior vary by type? If so, can a common interface avoid growing `if-else` logic?
