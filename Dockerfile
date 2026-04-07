## Stage 1: Build Solid.js frontend
FROM node:22-slim AS frontend
WORKDIR /app
COPY web/frontend/package.json web/frontend/package-lock.json* ./
RUN npm ci
COPY web/frontend/ .
RUN npm run build

## Stage 2: Python backend + static frontend
FROM python:3.12-slim
WORKDIR /app

COPY web/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY web/backend/ web/backend/
COPY --from=frontend /app/dist/ web/frontend/dist/

EXPOSE 8083

CMD ["python", "-m", "uvicorn", "web.backend.app:app", "--host", "0.0.0.0", "--port", "8083"]
