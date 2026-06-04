CREATE TABLE payment_intent (
  intent_id VARCHAR(36) PRIMARY KEY,
  merchant_id VARCHAR(36) NOT NULL,
  amount NUMERIC(18,2) NOT NULL,
  currency VARCHAR(10) NOT NULL,
  reference_id VARCHAR(255),
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);

CREATE INDEX idx_payment_intent_merchant_created ON payment_intent(merchant_id, created_at);
