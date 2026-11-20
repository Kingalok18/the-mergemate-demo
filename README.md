# Sample Shop

A tiny Spring Boot online shop used to demo MergeMate.

## Features
- User registration and login (`AuthController`, `AuthService`)
- Price preview with coupons and 18% tax (`CheckoutController`, `PriceCalculator`)
- Placing and cancelling orders (`OrderController`, `OrderService`)
- Stock checks (`InventoryService`), payments (`PaymentService`) and emails (`EmailService`)

## Run
```
mvn spring-boot:run
```
The app starts on port 8081.

## Coupons
| Code | Discount |
| --- | --- |
| WELCOME10 | 10% |
| FESTIVE25 | 25% |
