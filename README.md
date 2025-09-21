
# 🛍️ E-Commerce Backend – Spring Boot REST API

**A modern, scalable, and production-ready backend for a complete e-commerce platform.**  
Built using **Spring Boot**, this backend provides a set of **clean, secure, and well-structured APIs** for managing everything from **products, orders, and payments** to **wishlists, reviews, and customers**.

This project is designed to **power real-world e-commerce solutions**, whether it’s a web platform or a mobile shopping app, with a strong focus on **data integrity**, **transaction management**, and **maintainable code architecture**.

---

## 🚀 Why This Project Stands Out

Building an e-commerce system is challenging due to its **complex relationships** and **business rules**. This project demonstrates how to tackle these challenges by implementing:

- **Proper database relationships** with `OneToOne`, `OneToMany`, `ManyToOne`, and `ManyToMany`.
- **Transactional safety** to ensure data consistency.
- **Scalable and modular design**, making it easy to add new features.
- **Cascading and orphan removal** for automatic cleanup of related data.
- **Realistic workflows**, such as moving items from a wishlist to an order and handling payments.

> It’s not just another CRUD project — this backend mimics **real-world e-commerce scenarios**.

---

## ✨ Core Features

### **1. Product Management**
- Create, update, view, and delete products.
- Maintain accurate **inventory and stock levels**.
- Validate before deleting products:
  - Prevent deletion if the product is part of an **active order**.
  - Automatically delete related **wishlist items** and **reviews**.
  
> Ensures no dangling references or broken data relationships.

---

### **2. Customer Management**
- Full customer profiles with relationships to:
  - Orders
  - Wishlists
  - Reviews
- Clean deletion logic:
  - When a customer is deleted, their **reviews**, **wishlist items**, and **pending orders** are handled properly.

---

### **3. Wishlist Feature**
- Add or remove products from a wishlist.
- **Move products from wishlist directly to orders**:
  - Automatically reduces product stock.
  - Removes the item from the wishlist.
  - Creates a new order with **PENDING_PAYMENT** status.

> Provides a seamless “Add to Cart → Checkout” backend workflow.

---

### **4. Order Management**
- Create orders with **multiple items**.
- Automatically decrease product stock on order placement.
- Track order status through the entire lifecycle:
  ```
  PENDING_PAYMENT → PROCESSING → PAID → COMPLETED
  ```
- Built-in validations to prevent invalid operations such as paying for canceled orders.

---

### **5. Payment System**
- Securely process payments with unique **auto-generated confirmation codes** (e.g., `CONF-AB12CD34`).
- Support multiple payment modes such as **CREDIT_CARD** or **DEBIT_CARD**.
- Enforce a strict **one-to-one relationship between orders and payments**.
- Payment status tracking:
  - INITIATED
  - CONFIRMED
  - FAILED

---

### **6. Product Reviews**
- Customers can leave **only one review per product**.
- Reviews include:
  - **Rating** (1–5 stars)
  - **Title and detailed comments**
  - Automatic timestamp
- Automatically deleted if the associated **product** or **customer** is removed.

> Database-enforced uniqueness on `(customer_id, product_id)`.

---

## 🗄️ Database Design

This backend uses a **relational database** with clean, normalized tables and strong referential integrity.

```
Customer 1---* Wishlist *---1 Product
Customer 1---* Review   *---1 Product
Customer 1---* Order
Order    1---* OrderItem *---1 Product
Order    1---1 Payment
```

### **Key Tables:**
- **customers** – Stores customer details  
- **products** – All product-related data  
- **wishlists** – Tracks products customers are interested in  
- **orders** – Main order records  
- **order_items** – Line items for each order  
- **payments** – Payment details per order  
- **reviews** – Customer reviews per product  

---

## 📂 Project Structure

```
src/
├── controller/      # REST controllers (API endpoints)
├── service/         # Interfaces for business logic
├── serviceimpl/     # Implementations of services
├── entity/          # JPA entities and relationships
├── repository/      # Spring Data JPA repositories
├── request/         # Request DTOs for incoming data
├── response/        # Response DTOs for outgoing data
└── enumpackage/     # Enum classes for statuses and roles
```

> **Clean Architecture Principle:**  
> Each layer is independent, making the system maintainable and easy to test.

---

## 🌐 API Workflows & Sample JSON

### **1. Create a Product**
`POST /api/v1/products/create`
```json
{
  "title": "Next-Gen Gaming Console",
  "description": "8K support, ultra-fast SSD, enhanced graphics, and immersive 3D audio.",
  "price": 499.99,
  "quantity": 50
}
```

### **2. Add Product to Wishlist**
`POST /api/v1/wishlist/add-to-wishlist`
```json
{
  "customerId": 6,
  "productId": 3
}
```

### **3. Move Wishlist Item to Order**
`POST /api/v1/wishlist/move-to-order/wishlist`
```json
{
  "customerId": 6,
  "productId": 3,
  "quantity": 1,
  "shippingAddress": "1234 Elm Street, Springfield, IL 62701",
  "paymentMode": "CREDIT_CARD"
}
```

### **4. Make a Payment**
`POST /api/v1/payments/pay`
```json
{
  "orderId": 101,
  "mode": "CREDIT_CARD",
  "amount": 499.99
}
```

### **5. Leave a Product Review**
`POST /api/v1/reviews/add`
```json
{
  "productId": 3,
  "customerId": 2,
  "rating": 5,
  "title": "Outstanding Performance and Build Quality",
  "comment": "The performance is blazing fast, perfect for gaming and multitasking!"
}
```

---

## ⚙️ Tech Stack

| Technology       | Purpose |
|------------------|---------|
| **Spring Boot**  | Backend framework |
| **Spring Data JPA** | ORM and database access |
| **MySQL**        | Relational database |
| **Lombok**       | Reduces boilerplate code |
| **Maven**        | Build and dependency management |
| **Postman**      | API testing |

---

## 💡 Best Practices Implemented

- **Transaction Safety** – Ensures atomic operations for order creation and payment processing.
- **Cascade and Orphan Removal** – Automatically cleans up related records like reviews and wishlist entries.
- **Validation & Error Handling** – Clear exceptions and meaningful error messages.
- **Data Integrity Checks** – Prevent duplicate reviews, invalid payments, or stock inconsistencies.
- **Scalable Modular Design** – Each module is independent and easily extensible.

---

## 🔐 Business Rules

- **Product deletion:**  
  - Not allowed if linked to active orders.  
  - Automatically deletes related reviews and wishlist entries.

- **Wishlist to Order:**  
  - Reduces product stock automatically.  
  - Removes product from wishlist after moving to order.

- **Payments:**  
  - Only one payment per order.  
  - Unique auto-generated confirmation code.

- **Reviews:**  
  - Each customer can leave only **one review per product**.

---

## 🧾 Example Workflow

### Scenario:  
A customer moves a product from their wishlist to an order and makes a payment.

**Steps:**
1. Customer adds a product to the wishlist.  
2. Customer decides to buy the product and moves it to an order:
   - Stock is decreased.
   - Wishlist entry is removed.
   - Order is created with `PENDING_PAYMENT` status.
3. Customer pays for the order:
   - Payment is created with `INITIATED` → `CONFIRMED` status.
   - Order status updates to `PAID`.

---

## 🔮 Future Enhancements

This backend is built with scalability in mind. Potential future features include:
- **JWT Authentication** and role-based access (Admin, Customer)
- **Integration with real payment gateways** like Stripe or PayPal
- **Coupon and discount support**
- **Email notifications for orders**
- **Multi-currency and tax calculation**
- **Analytics dashboard for admins**

---

## 📌 Summary

This project demonstrates how to build a **real-world e-commerce backend** that is both powerful and maintainable.

| Feature            | Implementation |
|--------------------|----------------|
| Product Management | CRUD, inventory, safe deletion |
| Wishlist           | Add/remove items, seamless move to order |
| Orders             | Multi-item orders, stock updates, status tracking |
| Payments           | One-to-one order mapping, unique confirmation codes |
| Reviews            | Unique constraints, cascade deletion |
| Data Integrity     | Validation, business rules, and transaction safety |
