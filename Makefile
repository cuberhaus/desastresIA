.PHONY: install dev build docker-build docker-up docker-down docker-logs help

install: ## Install Python + frontend dependencies
	pip install -r web/requirements.txt
	cd web/frontend && npm install

dev: install ## Start backend + frontend dev servers (hot-reload)
	@echo "Starting backend on :8083 and frontend on :3000 ..."
	@trap 'kill 0' EXIT; \
	python -m uvicorn web.backend.app:app --reload --port 8083 & \
	cd web/frontend && npm run dev & \
	wait

build: ## Build frontend for production
	cd web/frontend && npm run build

docker-build: ## Build Docker image
	docker build -t desastres-ia .

docker-up: ## Start Docker container on :8083
	docker compose up -d
	@echo ""
	@echo "  DesastresIA is running at:"
	@echo "    ➜  http://localhost:8083"
	@echo ""

docker-down: ## Stop Docker container
	docker compose down

docker-logs: ## Tail Docker logs
	docker compose logs -f

help: ## Show this help
	@echo "DesastresIA — Local Search Solver"
	@echo ""
	@echo "Usage:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-16s\033[0m %s\n", $$1, $$2}'
