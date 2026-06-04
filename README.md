# PayPilot Sprint 3 + Sprint 4 Platform

This project is generated from `paypilot.pdf` Sprint 3 and Sprint 4. It decomposes the Sprint 1 modular monolith and Sprint 2 event-driven ledger work into independent services, then adds UI, containerization, Kubernetes, and cloud-portability features.

## Sprint 3 Slice Mapping

| Slice | Project Artifact |
| --- | --- |
| S3-1 Service Split + API Contracts | `merchant-service`, `payment-service`, `ledger-service`, `paypilot-common`, per-service OpenAPI YAML |
| S3-2 API Gateway Routing | `api-gateway` on port `8080`, routes to service ports `8081`, `8082`, `8083` |
| S3-3 Contract Tests | `payment-service/src/test/resources/contracts/payment-to-ledger` |
| S3-4 E2E Flow Harness | `e2e-tests` module with deterministic Testcontainers/WireMock skeleton |
| S3-5 Performance + Reliability | `performance/ledger-paging.js`, `docs/performance-baseline.md` |

## Sprint 4 Slice Mapping

| Slice | Project Artifact |
| --- | --- |
| S4-1 Angular Merchant Console | `paypilot-ui/src/app/merchant-console` |
| S4-2 Angular Payments Ops | `paypilot-ui/src/app/payments-ops` |
| S4-3 Containerization | Service Dockerfiles, `paypilot-ui/Dockerfile`, `docker-compose.yml`, `RUNBOOK.md` |
| S4-4 Kubernetes Manifests | `k8s/` namespace, ConfigMap, Secret, Deployments, Services, probes |
| S4-5 Cloud Portability | `ledger-service` storage abstraction, mock S3 profile, feature flag, graceful shutdown |

## Services

| Service | Port | Boundary |
| --- | ---: | --- |
| merchant-service | 8081 | Merchant onboarding and KYC |
| payment-service | 8082 | Payment intents and simulated authorization |
| ledger-service | 8083 | Append-only ledger and settlement views |
| api-gateway | 8080 | Client entry point, routing, correlation header propagation |

## Run Locally

```powershell
mvn clean test
mvn -pl merchant-service spring-boot:run
mvn -pl payment-service spring-boot:run
mvn -pl ledger-service spring-boot:run
mvn -pl api-gateway spring-boot:run
```

The default profile uses per-service H2 databases so the services can boot without external infrastructure. `docker-compose.yml` provides the Sprint 3 Postgres/Kafka shape for teams ready to wire real local infrastructure.

## Run With Docker Compose

```powershell
docker compose up --build
```

Open the Sprint 4 UI at `http://localhost`. The UI proxies `/api/**` requests to the API gateway service.

## Kubernetes

```powershell
kubectl apply -f k8s/
kubectl -n paypilot get pods
```

## CI Script

Run tests across the three Sprint 3 microservices:

```powershell
.\scripts\ci-services.ps1
```

If PowerShell script execution is disabled, use:

```powershell
.\scripts\ci-services.cmd
```

The script runs `mvn -pl merchant-service,payment-service,ledger-service -am test`, so `paypilot-common` is included automatically.

## Boundary Rules

- No service imports another service's entity classes.
- No shared tables across services.
- `paypilot-common` contains only DTO-style API error handling, correlation utilities, and event payload records.
- Cross-service integration should use HTTP contracts or Kafka events, not database access.

## Breaking Change Guidance

A change is breaking if it removes a field, changes a field type, renames an endpoint, changes response status semantics, or changes pagination/error shapes. Add a new versioned API or contract instead of changing `v1` in place.

## Innovation Add-On

**Problem:** Sprint 3 often fails because route, contract, and performance evidence live in different places.

**Choice:** This scaffold includes a single `docs/sprint3-evidence.md` checklist that maps each slice to concrete evidence.

**Validation:** Fill the checklist after running service smoke tests, contract verification, E2E tests, and the k6 ledger benchmark.
