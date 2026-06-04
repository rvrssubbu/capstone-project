CREATE TABLE ledger_entry (
  entry_id VARCHAR(36) PRIMARY KEY,
  merchant_id VARCHAR(36) NOT NULL,
  intent_id VARCHAR(36) NOT NULL,
  amount NUMERIC(18,2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  direction VARCHAR(10) NOT NULL,
  reference VARCHAR(255),
  entry_time TIMESTAMP NOT NULL
);

CREATE TABLE event_dedupe (
  dedupe_key VARCHAR(128) PRIMARY KEY,
  processed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_ledger_merchant_time ON ledger_entry(merchant_id, entry_time DESC, entry_id DESC);
