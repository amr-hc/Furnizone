# E-Commerce (Spring Boot + Angular)

This is a **Spring Boot-based E-Commerce** that provides backend services for an online store. The API supports **user authentication**, **product management**, **cart functionality**, **orders**, and more.

---

## Features

✅ **User Authentication** (JWT-based authentication)
✅ **Product Management** (CRUD operations, pagination, search)
✅ **Shopping Cart** (Add/remove products, manage quantity)
✅ **Orders** (Place orders, manage order status)
✅ **Admin Panel Support** (Only admins can manage products & orders)
✅ **Secure API** (Spring Security with role-based access control)
✅ **CORS Configuration** (Supports frontend integration with Angular)

---

## 📌 Tech Stack

### **Backend** (Spring Boot)

- **Spring Boot 3+**
- **Spring Security (JWT Authentication)**
- **Spring Data JPA (Hibernate, MySQL)**
- **Spring Web (RESTful API)**
- **Lombok (for clean code)**
- **Maven (Dependency Management)**

### **Frontend** (Angular 17)

- **Angular (TypeScript Framework)**
- **Bootstrap (UI Design)**
- **RxJS (Observables & HTTP Requests)**
- **Angular Router (Navigation & Routing)**

---

## 🚀 Getting Started

### **1️⃣ Clone the Repository**

```bash
 git clone https://github.com/amr-hc/Furnizone.git
 cd Furnizone/backend
```

### **2️⃣ Setup Database (MySQL)**

Create a database in MySQL:

```sql
CREATE DATABASE ecommerce_db;
```

### \*\*3️⃣ Configure ****`.env`**** or \*\***`application.properties`**

Modify `src/main/resources/application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
app.base-url=http://localhost:8080
```

### **4️⃣ Run the Backend**

```bash
mvn spring-boot:run
```

Backend will be available at: **`http://localhost:8080/api/v1`**

### \*\*5️⃣ Setup Angular Frontend \*\*

Clone the Angular frontend:

```bash
git clone https://github.com/amr-hc/Furnizone.git
cd Furnizone/front-end
npm install
ng serve
```

Frontend will be available at: **`http://localhost:4200`**

