# Interface vs Abstract Class — LLD Revision Notes

## Table of Contents

- [Core Difference](#core-difference)
- [1. Interface Example — Payment Contract](#1-interface-example--payment-contract)
- [2. Abstract Class Example — Shared Workflow](#2-abstract-class-example--shared-workflow)
- [3. Quick Comparison](#3-quick-comparison)
- [4. LLD Decision Rule](#4-lld-decision-rule)
- [5. Common Interview Trap](#5-common-interview-trap)
- [Interview Recall](#interview-recall)

## Core Difference

- **Interface:** Defines a contract — what a class can do.
- **Abstract class:** Provides a shared base — common state and behavior.

**Remember:** Interface = capability; abstract class = shared foundation.

## 1. Interface Example — Payment Contract

```java
interface PaymentProcessor {
    void pay(double amount);
}

class UpiPayment implements PaymentProcessor {
    @Override
    public void pay(double amount) {
        System.out.println("Paying via UPI");
    }
}

class CardPayment implements PaymentProcessor {
    @Override
    public void pay(double amount) {
        System.out.println("Paying via Card");
    }
}
```

**Use an interface when:**
- Different classes need the same contract.
- Implementations may be unrelated.
- You need interchangeable implementations.
- A class may need to implement multiple capabilities.

Examples: `PaymentProcessor`, `NotificationSender`, `PricingStrategy`.

## 2. Abstract Class Example — Shared Workflow

```java
abstract class AbstractPaymentProcessor {

    public void processPayment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        System.out.println("Logging payment");
        pay(amount);
    }

    protected abstract void pay(double amount);
}

class UpiPayment extends AbstractPaymentProcessor {
    @Override
    protected void pay(double amount) {
        System.out.println("Paying via UPI");
    }
}
```

Usage:

```java
AbstractPaymentProcessor processor = new UpiPayment();
processor.processPayment(1000);
```

Output:
```text
Logging payment
Paying via UPI
```

The parent provides the common workflow; the child implements the varying step. This is an example of the **Template Method pattern**.

**Use an abstract class when:**
- Related classes share state or implementation.
- Common helper methods belong in a base class.
- You want a shared workflow with customizable steps.

## 3. Quick Comparison

| Feature | Interface | Abstract Class |
|---|---|---|
| Purpose | Define a contract | Share state and behavior |
| Methods | Abstract, `default`, `static`, private | Abstract and concrete |
| Instance fields | No instance fields; fields are constants (`public static final`) | Can have instance fields |
| Constructor | No | Yes |
| Multiple inheritance | A class can implement multiple interfaces | A class can extend only one class |
| Access | Abstract contract methods are implicitly `public` | Methods/fields can use different access modifiers |

**Note:** Interfaces can contain implemented `default`, `static`, and private methods. They are not limited to abstract methods.

## 4. LLD Decision Rule

Ask:

1. Do I only need a common contract? → **Interface**
2. Do related classes need meaningful shared state or implementation? → **Consider an abstract class**
3. Do I need both a contract and shared implementation? → A class can implement an interface and extend an abstract class.

## 5. Common Interview Trap

For email and SMS notifications, use a `NotificationSender` interface if all you need is a common `send()` contract.

Don't create an abstract class just to avoid a small amount of duplication. Shared behavior should genuinely belong in the base class.

## Interview Recall

- **Interface:** What capability must this class provide?
- **Abstract class:** What state or behavior can related classes share?
- **Interface vs abstract class:** Contract vs shared foundation.
