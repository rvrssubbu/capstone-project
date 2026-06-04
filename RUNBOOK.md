# PayPilot Sprint 4 Runbook

## Docker Compose

Start all services and the UI:

```powershell
docker compose up --build
```

Open:

- UI: `http://localhost`
- Gateway: `http://localhost:8080`
- Merchant service: `http://localhost:8081`
- Payment service: `http://localhost:8082`
- Ledger service: `http://localhost:8083`

## Kubernetes

Build local images first:

```powershell
docker compose build
```

For kind or minikube, load/tag images according to your local cluster workflow, then apply:

```powershell
kubectl apply -f k8s/
kubectl -n paypilot get pods
kubectl -n paypilot rollout status deployment/api-gateway
```

## Troubleshooting

- DB not ready: run `docker compose logs merchant-db` and wait for the health check.
- Gateway returns 503: check `docker compose logs api-gateway` and confirm service URLs use Docker service names.
- UI cannot call API: confirm gateway is on `localhost:8080` and CORS allows `http://localhost`.
- Kubernetes pod not ready: run `kubectl -n paypilot describe pod <pod-name>` and inspect probe failures.
- Feature flag disabled: set `FEATURE_SETTLEMENT_EXPORT=true` before starting `ledger-service`.
