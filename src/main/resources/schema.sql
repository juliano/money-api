CREATE TABLE IF NOT EXISTS Account(
    id BIGINT,
    amount DECIMAL(20, 2),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Transaction(
    id BIGINT,
    created_at TIMESTAMP,
    source BIGINT,
    destination BIGINT,
    amount DECIMAL(20, 2)
);