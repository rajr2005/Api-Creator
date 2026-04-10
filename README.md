# MockForge

MockForge is a starter project built with Spring Boot and React for creating and serving custom mock APIs.

## Features

- Create mock APIs with custom path, method, headers, status code, and response body
- Store mock API definitions in memory for quick prototyping
- Test generated mock APIs from the React dashboard
- Serve mock responses from dynamic URLs under `/mock/*`

## Project structure

- `src/main/java`: Spring Boot backend
- `frontend`: React + Vite frontend

## Run the backend

```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`.

## Run the frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend will start on `http://localhost:5173`.

## Example

Create a mock API in the UI with:

- Method: `GET`
- Path: `/users`
- Status: `200`
- Content type: `application/json`

Then call:

```bash
curl http://localhost:8080/mock/users
```
