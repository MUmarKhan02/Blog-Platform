# Blog Platform – Full Stack Application

A **full-stack blog platform** built with **Spring Boot (Java)** for the backend and **React (TypeScript + Vite + Tailwind CSS)** for the frontend, using **PostgreSQL / MySQL** as the database.

This project demonstrates:

- REST API development  
- User Authentication (JWT + Refresh Token)  
- Clean Architecture  
- Modern Frontend Integration  
- Create / Edit Posts  
- Draft Posts (or publish after editing)  
- Categories and Tags  
- Light / Dark Mode UI  

---

## Tech Stack

**Backend:**

- Java 17+  
- Spring Boot  
- Spring Security (JWT + Refresh Token)  
- JPA / Hibernate  
- Maven  
- MySQL / PostgreSQL  
- Docker (optional)  

**Frontend:**

- React  
- TypeScript  
- Vite  
- Tailwind CSS  
- Axios  

---

## Project Structure

blog-platform/
├── blog/ # Spring Boot backend
├── frontend/ # React frontend
└── README.md


---

## Backend Setup (Spring Boot)

### Requirements

- Java 17+  
- Maven  

> **Note:** IntelliJ is recommended as it has built-in support for Lombok. Other IDEs (e.g., VSCode) require extra setup for Lombok.

### Setup

1. Navigate to the backend folder:

```bash
cd blog
```
2. Run the backend:

```bash
mvn spring-boot:run
```
3. Backend will start on
```bash http://localhost:8080```

## Frontend Setup (React)

### Requirements

- Node.js (v16+)  
- npm (or yarn)  

### Setup

1. Navigate to the frontend folder:

```bash
cd frontend
```
2. Install dependencies:

```bash
npm install
```
3. Create a .env file in the frontend folder with the backend API URL:

```bash
VITE_API_URL=http://localhost:8080
```
4. Start the development server:

```bash
npm run dev
```
5. The frontend will be available at:

```bash
http://localhost:5173
```
# Frontend and Backend Together
1. Start backend

```bash
cd ../blog
mvn spring-boot:run
```
2. Start frontend in a separate terminal

```bash npm run dev ```
3. Open browser at
```bash http://localhost:5173 ```


## Future Changes or Improvements 
- Adding admin or user roles for users to manage restrictions and realism
- Email verification on registration via link or OTP
- Password reset functionality
- User profile page customization and avatar support
