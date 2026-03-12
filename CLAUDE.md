# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Backend (Spring Boot)
```bash
./mvnw spring-boot:run        # Run the application (port 8080)
./mvnw clean package          # Build the JAR
./mvnw test                   # Run all tests
./mvnw test -Dtest=ClassName  # Run a single test class
```

### Frontend (React + Vite)
```bash
cd expense-tracker-frontend
npm install                   # Install dependencies
npm run dev                   # Dev server (port 5173)
npm run build                 # Production build
```

### Prerequisites
- Java 17+
- MySQL running on `localhost:3306` with database `expense_tracker`, credentials `root/root`
- Node.js 18+ for frontend

## Architecture

Full-stack expense tracker with a Spring Boot REST API backend and React (Vite) frontend.

### Backend Package Layout (`src/main/java/com/expensetracker/`)
- `controller/` — REST endpoints: `AuthController` (`/auth/**`) and `ExpenseController` (`/expenses/**`)
- `service/` — Business logic: `UserService`, `ExpenseService`
- `repository/` — Spring Data JPA repositories for `User` and `Expense`
- `entity/` — JPA entities: `User` (one-to-many) ↔ `Expense` (many-to-one)
- `security/` — `JwtService`: HS256 JWT generation, 1-hour expiration, email as subject
- `config/` — `SecurityConfig`: CSRF disabled, `/auth/**` public, `/expenses/**` currently open

### Data Model
- `User`: id, name, email, password, role ("USER"), expenses list
- `Expense`: id, title, amount (Double), category, date (LocalDate), user (FK)

### API Endpoints
| Method | Path | Description |
|--------|------|-------------|
| POST | `/auth/register` | Register user, returns User |
| POST | `/auth/login` | Authenticate, returns JWT string |
| POST | `/expenses` | Create expense |
| GET | `/expenses` | List all expenses |

### Frontend (`expense-tracker-frontend/src/`)
- `App.jsx` — Root component with routing/state
- `components/AddExpense.jsx` — Expense creation form
- `components/ExpenseList.jsx` — Expense display
- `services/api.js` — Centralized Axios/fetch calls to backend

### Known Limitations (early development)
- Passwords stored as plain text (no hashing)
- `ExpenseService.saveExpense()` hardcodes user ID 1 instead of using the authenticated user
- JWT secret is hardcoded as `"mysecretkey"` in `JwtService`
- `/expenses/**` is unauthenticated in `SecurityConfig` (marked temporary)
- No input validation on API endpoints
