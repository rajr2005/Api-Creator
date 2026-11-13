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

## Deploy

### Backend on Render

1. Create a new Render Web Service from this repository and use the repository root as the service directory. Render will use the included `Dockerfile` (also described in `render.yaml`).
2. Set `CORS_ALLOWED_ORIGINS` to the full Vercel site origin, for example `https://mockforge.vercel.app`. For multiple frontend origins, separate them with commas.
3. After deployment, copy the backend URL, such as `https://mockforge-api.onrender.com`.

The service reads Render's `PORT` environment variable. Its health check uses `GET /api/mock-endpoints`.
Mock endpoint definitions are currently stored in memory, so they reset whenever the backend restarts or redeploys. Add persistent storage before relying on this deployment for data you need to keep.

### Frontend on Vercel

1. Import the repository into Vercel and set the Root Directory to `frontend`.
2. The included Vercel rewrites forward `/api/*` and `/mock/*` requests to `https://api-creator.onrender.com`. Update those destinations in `frontend/vercel.json` if your backend URL changes.
3. Deploy or redeploy. Leave `VITE_API_URL` unset so browser requests use the same Vercel origin and avoid cross-origin CORS requests.

For local development, leave `VITE_API_URL` unset to use the Vite proxy.
