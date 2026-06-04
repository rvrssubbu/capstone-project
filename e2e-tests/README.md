# Sprint 3 E2E Harness

This module is the landing zone for S3-4.

Target deterministic flow:

1. Start Postgres and Kafka with Testcontainers.
2. Start or attach to merchant-service, payment-service, ledger-service, and api-gateway.
3. Stub provider authorization with WireMock.
4. Run: create merchant -> verify KYC -> create payment intent -> wait for async processing -> verify ledger entry.
5. Repeat 3 times with Awaitility-based waits.

The included test class is disabled until service startup strategy is chosen: local services, Spring contexts per module, or containerized service images.
