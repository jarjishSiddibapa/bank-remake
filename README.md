# Bank-Remake

An enterprise-grade **bank management system** built with Java and Spring Boot, featuring RESTful APIs for user, account, and transaction management, integrated with PostgreSQL and secured via Spring Security.

## 🚀 Key Features

- **User Management**: Register, authenticate, and manage user profiles
- **Account Management**: Create and manage checking and savings accounts
- **Transaction Processing**: Perform deposits, withdrawals, and transfers with validation
- **Role-Based Security**: Secured endpoints for `ADMIN` and `USER` roles via Spring Security
- **Layered Architecture**: Clear separation of concerns with Controller, Service, Repository, DTO, Entity, and Exception layers
- **Automated Schema Updates**: Hibernate auto DDL generation for seamless database migrations
- **Configurable**: Externalized configuration via `application.properties`

## 📋 Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Contributing](#contributing)
- [License](#license)

## 🛠 Tech Stack

- **Java 17** - Core programming language
- **Spring Boot** - Main framework
  - Spring Web (REST API)
  - Spring Data JPA
  - Spring Security
- **PostgreSQL** - Database
- **Maven** - Build tool
- **JUnit & Mockito** - Testing framework

## 📁 Project Structure

```
bank-remake/
├── .mvn/wrapper/
├── src/
│   ├── main/
│   │   ├── java/com/aurionpro/bankremake/
│   │   │   ├── config/         # Application configuration classes
│   │   │   ├── controller/     # REST controllers
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── BankAccountController.java
│   │   │   │   └── TransactionController.java
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── entity/         # JPA entity classes
│   │   │   ├── enums/          # Application enums (AccountType, Role, etc.)
│   │   │   ├── exception/      # Custom exceptions and handlers
│   │   │   ├── repository/     # Spring Data JPA repositories
│   │   │   ├── security/       # Security configuration (JWT, PasswordEncoder)
│   │   │   ├── service/        # Business logic services
│   │   │   └── BankRemakeApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/aurionpro/bankremake/  # Unit and integration tests
├── mvnw, mvnw.cmd             # Maven wrapper scripts
├── pom.xml                    # Maven configuration
├── .gitignore
├── .gitattributes
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/jarjishSiddibapa/bank-remake.git
   cd bank-remake
   ```

2. **Configure database**
   Create a PostgreSQL database named `bank_remake_db` and update credentials in `src/main/resources/application.properties`.

3. **Build and run**
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

The application will start on **http://localhost:8080** by default.

## ⚙️ Configuration

Edit `src/main/resources/application.properties` to customize:

```properties
spring.application.name=bank-remake

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/bank_remake_db
spring.datasource.username=postgres
spring.datasource.password=1234

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin123

# Optional: API Context Path
#server.servlet.context-path=/api/v1
```

## 🌐 API Endpoints

### User Management
- `POST /api/users/register` – Register a new user
- `POST /api/users/login` – Authenticate and receive JWT
- `GET /api/users/{id}` – Retrieve user details (secured)

### Bank Account Management
- `POST /api/accounts` – Create new account
- `GET /api/accounts/{id}` – Get account details
- `GET /api/accounts/user/{userId}` – List accounts for a user

### Transaction Management
- `POST /api/transactions/deposit` – Deposit funds
- `POST /api/transactions/withdraw` – Withdraw funds
- `POST /api/transactions/transfer` – Transfer between accounts
- `GET /api/transactions/account/{accountId}` – Transaction history

### Admin Operations
- `GET /api/admin/users` – List all users (ADMIN only)
- `GET /api/admin/accounts` – List all accounts (ADMIN only)

## 🧪 Running Tests

Execute the test suite with:

```bash
./mvnw test
```

For specific test categories:
```bash
# Unit tests only
./mvnw test -Dtest="*Test"

# Integration tests only
./mvnw test -Dtest="*IT"
```

## 📊 Database Schema

The application uses PostgreSQL with the following main entities:
- **Users** - User account information
- **BankAccounts** - Customer bank accounts
- **Transactions** - Financial transaction records
- **Roles** - User role management

## 🔐 Security Features

- **JWT Authentication** - Stateless authentication mechanism
- **Role-based Authorization** - Different access levels for users and admins
- **Password Encryption** - BCrypt password hashing
- **Input Validation** - DTO validation with Spring Boot Validation
- **Custom Exception Handling** - Centralized error management

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Jarjish Siddibapa**
- GitHub: [@jarjishSiddibapa](https://github.com/jarjishSiddibapa)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community for the robust database
- AurionPro for the development environment and learning opportunities

---

**Note**: This is a learning project built during training at AurionPro. It demonstrates enterprise Java development practices and modern Spring Boot application architecture.