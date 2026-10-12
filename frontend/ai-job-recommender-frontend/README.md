# CareerLens AI — React frontend

A standalone React + Vite frontend for the JobSeeker Spring Boot project. It includes:

- PDF resume upload and ranked job matches
- Skill-match percentage, matched skills, and missing skills
- Match filters and a compact result summary
- Ask AI panel connected to the existing Gemini-backed endpoint
- Responsive layout for desktop, tablet, and mobile

## Requirements

- Node.js 20.19+ or 22.12+ recommended for current Vite versions
- Spring Boot backend running on `http://localhost:8080`

## Run locally

From this directory:

```bash
npm install
cp .env.example .env
npm run dev
```

Open `http://localhost:5173`.

Set `VITE_API_BASE_URL` in `.env` if your backend runs at a different URL:

```env
VITE_API_BASE_URL=http://localhost:8080
```

Restart Vite after editing `.env`.

## Backend endpoints used

Resume upload and recommendations:

```http
POST /api/recommendations/resume
Content-Type: multipart/form-data
file=<resume.pdf>
```

Expected response: an array of objects containing `jobId`, `profile`, `description`, `experience`, `requiredSkills`, `matchedSkills`, `missingSkills`, and `matchPercentage`.

Ask AI:

```http
GET /ai/ask?question=...
```

The Ask AI endpoint is expected to return a text response.

## If browser requests are blocked

The browser may block requests when the frontend (`http://localhost:5173`) and backend (`http://localhost:8080`) use different origins. Configure CORS for the frontend origin in Spring Boot and ensure your Spring Security rules permit the endpoints you intend to expose. For a quick local development test, `@CrossOrigin(origins = "http://localhost:5173")` can be added to the relevant controllers, subject to your security configuration. Do not treat open CORS as a production security policy.

## Build production assets

```bash
npm run build
npm run preview
```

The generated static site is written to `dist/`.
