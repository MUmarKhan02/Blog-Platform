# Khan's Blog Platform

A **full-stack blog platform** built with **Spring Boot (Java 21)** on the backend and **React (TypeScript + Vite + Tailwind CSS)** on the frontend, deployed on **AWS**.

🌐 **Live Demo:** [https://d3djg0s7uij4hb.cloudfront.net](https://d3djg0s7uij4hb.cloudfront.net)

---

## Features

- **Authentication** — JWT-based login and registration with refresh tokens
- **Role-Based Access Control** — Admin users can manage categories and tags; authors can edit/delete their own posts
- **Rich Text Editor** — TipTap-powered editor with headings, bold, italic, bullet/ordered lists
- **Post Management** — Create, edit, delete, and draft posts with reading time estimation
- **Categories & Tags** — Filter posts by category and tag on the home page
- **Search** — Debounced real-time search across post titles and content
- **Dark / Light Mode** — Full theme toggle with persistent preference
- **Toast Notifications** — Real-time feedback on all user actions
- **Password Validation** — Live checklist enforcing strength requirements on registration
- **Profile Page** — View your published posts and account details
- **404 Page** — Custom not-found page with navigation options
- **Empty States** — Friendly UI when no posts match filters or search
- **Loading Skeletons** — Smooth loading states for post lists

---

## Tech Stack

### Backend
- Java 21
- Spring Boot 3
- Spring Security (JWT + Refresh Token)
- JPA / Hibernate
- Maven
- PostgreSQL

### Frontend
- React 18
- TypeScript
- Vite
- Tailwind CSS + NextUI
- TipTap (rich text editor)
- Axios
- React Hot Toast
- DOMPurify

### Infrastructure (AWS)
- **CloudFront** — CDN for frontend delivery
- **S3** — Static frontend hosting
- **Elastic Beanstalk** — Spring Boot backend hosting
- **RDS (PostgreSQL)** — Managed production database

---

## Local Development Setup

### Requirements
- Java 21+
- Maven
- Node.js (v18+)
- Docker (for local PostgreSQL)

> **Note:** IntelliJ is recommended for the backend — it has built-in Lombok support.

### Backend Setup

1. Start the local database:
```bash
docker-compose up -d
```

2. Navigate to the backend folder:
```bash
cd blog
```

3. Add environment variables in your IntelliJ run configuration:
```
JWT_SECRET=your-secret-key-at-least-32-characters
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your-password
```

4. Run the backend:
```bash
./mvnw spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend folder:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Create a `.env` file:
```
VITE_API_URL=http://localhost:8080
```

4. Start the dev server:
```bash
npm run dev
```

Frontend runs on `http://localhost:5173`

---

## Admin Access

By default all registered users have the `USER` role. To grant admin access, run the following SQL against the database:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your@email.com';
```

Admin users can create, edit, and delete categories and tags.

---

## Deployment

The application is deployed on AWS:

| Service | Purpose |
|---|---|
| S3 | Frontend static file hosting |
| CloudFront | HTTPS CDN, routes `/api/*` to backend |
| Elastic Beanstalk | Spring Boot JAR hosting (t3.micro) |
| RDS PostgreSQL | Production database (db.t3.micro) |

### Deploy Frontend
```bash
cd frontend
npm run build
# Upload dist/ contents to S3 bucket
# Invalidate CloudFront cache: /*
```

### Deploy Backend
```bash
cd blog
./mvnw package -DskipTests
# Upload target/blog-0.0.1-SNAPSHOT.jar to Elastic Beanstalk
```
