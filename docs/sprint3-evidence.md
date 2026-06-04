# Sprint 3 Evidence Checklist

## S3-1 Service Split + API Contracts

- All three services start locally.
- `GET /actuator/health` returns `UP` on ports 8081, 8082, and 8083.
- OpenAPI specs exist under each service's `openapi/` directory.

## S3-2 API Gateway Routing

- Gateway routes `/api/v1/merchants/**`, `/api/v1/payments/**`, and `/api/v1/ledger/**`.
- `X-Correlation-Id` appears in gateway response headers.
- Gateway routing test passes.

## S3-3 Consumer-Driven Contract Tests

- At least two payment-to-ledger contracts are verified.
- A deliberate provider response break fails the build, then is reverted.

## S3-4 E2E Flow Harness

- E2E test creates merchant, verifies KYC, creates payment, waits for authorization, and verifies ledger entry.
- Test passes 3 consecutive runs.
- Surefire report is published.

## S3-5 Performance + Reliability

- `performance/ledger-paging.js` records p50, p95, p99, and error rate.
- Duplicate replay of 1,000 `PaymentAuthorized` events does not create duplicate ledger rows.
- `docs/performance-baseline.md` contains before/after numbers.
