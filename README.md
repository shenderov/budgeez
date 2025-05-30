# Budgeez

**Budgeez** is a full-stack personal finance and budget management application.  
Built with Java Spring Boot (backend), MySQL (database), AngularJS and Bootstrap (frontend).

> **Disclaimer:**  
> This project was originally developed as a pet project during my Java studies.  
> It is primarily for demo purposes and may not represent my current coding standards or best practices.  
> I am capable of building much more robust and scalable systems today.  
> **The project is provided as-is, without any warranties.**

The application lets users track expenses, manage budgets, and gain insights into their financial habits.

---

## 🚀 Features

- User authentication using JWT (io.jsonwebtoken)
- Manage budgets and expense categories
- Track and visualize expenses
- RESTful API backend
- Responsive UI with AngularJS and Bootstrap
- API integration with MySQL database
- Comprehensive testing with TestNG and Rest Assured

---

## 🛠️ Tech Stack

- **Backend:** Java, Spring Boot, Spring Data JPA, Spring Security, io.jsonwebtoken (JWT)
- **Frontend:** AngularJS, Bootstrap
- **Database:** MySQL
- **App Server:** Apache Tomcat
- **Testing:** TestNG, Rest Assured

---

## 📁 Project Structure

<pre>
budgeez/
├── src/
│   ├── main/
│   │   ├── java/                 # Java source code (Spring Boot application)
│   │   └── resources/            # Configuration files, static resources, templates
│   └── test/                     # Automated tests (TestNG, Rest Assured)
├── pom.xml                       # Maven build configuration
├── README.md                     # Project documentation
└── ...
</pre>

- **src/main/java/** – All Java code for backend logic, controllers, services, and entities.
- **src/main/resources/** – Spring Boot configs (`application.properties`), static files, templates.
- **src/test/** – Automated backend and API tests using TestNG and Rest Assured.
- **pom.xml** – Maven project descriptor (dependencies, plugins, build info).
- **README.md** – This file; documentation and setup info.

---

## ⚡ Getting Started

### Prerequisites

- Java 8 or newer
- Maven
- MySQL
- Node.js & npm (for AngularJS frontend, if run separately)
- Apache Tomcat (or run with embedded Tomcat via Spring Boot)

### Backend Setup

1. **Clone the repository:**
    ```bash
    git clone https://github.com/shenderov/budgeez.git
    cd budgeez
    ```

2. **Configure MySQL:**
    - Create a database for the app:
      ```sql
      CREATE DATABASE budgeez DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
      ```
    - Update `src/main/resources/application.properties` with your DB credentials:
      ```
      spring.datasource.url=jdbc:mysql://localhost:3306/budgeez
      spring.datasource.username=your_username
      spring.datasource.password=your_password
      ```

3. **Build and run backend:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    The API should be available at [http://localhost:8080](http://localhost:8080).

### Frontend

If the AngularJS frontend is in a separate `src/main/resources/static` folder

---

## 🔒 Authentication

- Uses JWT (JSON Web Tokens) for securing API endpoints.
- After login, include the token in the `Authorization` header as `Bearer <token>` for protected requests.

---

## 🧪 Testing

- **Unit and integration tests:** TestNG
- **API tests:** Rest Assured
- To run all tests:
    ```bash
    mvn test
    ```
---

## 📝 License

This project is licensed under the [MIT License](LICENSE).

---

> _Questions or issues? Open an issue or start a discussion in this repository._
