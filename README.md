# Bank Account Management App

This is a simple banking application built with **Spring Boot** (backend) and **React.js + Bootstrap** (frontend).  
It demonstrates **OOP & SOLID principles** in the backend and provides a UI to manage bank accounts, deposits, withdrawals, transfers, and interest application.

## Features

- Create bank accounts (SAVINGS or CHECKING)
- View all accounts
- Deposit and withdraw money
- Transfer money between accounts
- Apply interest to **SAVINGS accounts only**
- Error handling in frontend to prevent React crashes
- Loading indicators while backend operations are in progress

## Backend (Spring Boot)

### Requirements

- Java 17+
- Maven
- H2 in-memory database

### Setup & Run

1. Clone the repository:
   ```
   git clone <repo-url>
   cd account-management-system
   cd account-management-backend
   ```

2. Build and run:
   ```
   mvn clean install
   mvn spring-boot:run
   ```

3. Backend runs on:
   ```
   http://localhost:8080
   ```

### APIs
---

## **1. Create Account**

```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "type": "SAVINGS",
    "ownerName": "alice@example.com"
  }'

curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "type": "CHECKING",
    "ownerName": "bob@example.com"
  }'
```

---

## **2. Get All Accounts**

```bash
curl -X GET http://localhost:8080/api/accounts
```

---

## **3. Get Account by ID**

```bash
curl -X GET http://localhost:8080/api/accounts/1
```

---

## **4. Deposit Money**

(amount is in **paise**, so `50000` = ₹500.00)

```bash
curl -X POST http://localhost:8080/api/accounts/1/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 1,
    "amountInPaise": 50000
  }'
```

---

## **5. Withdraw Money**

```bash
curl -X POST http://localhost:8080/api/accounts/1/withdraw \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": 1,
    "amountInPaise": 20000
  }'
```

---

## **6. Transfer Money Between Accounts**

```bash
curl -X POST http://localhost:8080/api/accounts/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "fromAccountId": 1,
    "toAccountId": 2,
    "amountInPaise": 12050
  }'
```

---

## **7. Apply Monthly Interest**

(Only applies interest if the account type supports it, e.g., **SAVINGS**)

```bash
curl -X POST http://localhost:8080/api/accounts/1/apply-interest
```

---

## Summary

| Method | Endpoint                            | Description                                |
| ------ | ----------------------------------- | ------------------------------------------ |
| POST   | `/api/accounts`                     | Create a new account (SAVINGS or CHECKING) |
| GET    | `/api/accounts`                     | Get all accounts                           |
| GET    | `/api/accounts/{id}`                | Get account by ID                          |
| POST   | `/api/accounts/{id}/deposit`        | Deposit money (amount in paise)            |
| POST   | `/api/accounts/{id}/withdraw`       | Withdraw money (amount in paise)           |
| POST   | `/api/accounts/transfer`            | Transfer money between accounts            |
| POST   | `/api/accounts/{id}/apply-interest` | Apply interest (SAVINGS only)              |

## Frontend (React + Bootstrap)

### Setup & Run

1. Navigate to frontend folder:

   ```bash
   cd account-management-frontend
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start the frontend:

   ```bash
   npm start
   ```

4. Frontend runs on:

   ```
   http://localhost:3000
   ```
