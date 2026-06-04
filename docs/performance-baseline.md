# Ledger Paging Performance Baseline

Endpoint: `GET /api/v1/ledger/entries?merchantId=merchant-1&page=0&size=20`

| Run | Dataset | VUs | Duration | p50 | p95 | p99 | Error Rate | Notes |
| --- | --- | ---: | --- | ---: | ---: | ---: | ---: | --- |
| Before optimization | 10,000 entries / 10 merchants | 50 | 60s | TBD | TBD | TBD | TBD | Capture first run |
| After optimization | 10,000 entries / 10 merchants | 50 | 60s | TBD | TBD | TBD | TBD | Add index or query fix |

## EXPLAIN ANALYZE

Paste the query plan here and note whether `idx_ledger_merchant_time` is used.

## Reliability Replay

Replay 1,000 duplicate `PaymentAuthorized` events and record:

- Unique payment count:
- Ledger row count after replay:
- Deduplication evidence:
