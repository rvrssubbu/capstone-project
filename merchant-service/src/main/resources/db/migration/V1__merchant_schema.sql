CREATE TABLE merchant (
  merchant_id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  category VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  phone VARCHAR(50),
  kyc_status VARCHAR(30) NOT NULL,
  reason VARCHAR(30),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP
);
