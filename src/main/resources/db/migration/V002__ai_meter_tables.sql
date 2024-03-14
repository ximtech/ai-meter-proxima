CREATE TABLE meter.ai_meter_config
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL,

    device_name VARCHAR(255) NOT NULL,
    device_ip VARCHAR(32),
    device_time_zone VARCHAR(128),
    cron_expression VARCHAR(32),
    last_execution_time TIMESTAMP
);

CREATE TABLE meter.ai_meter_device
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    created_by_user VARCHAR(255),
    updated_by_user VARCHAR(255),
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL,
    
    device_id UUID NOT NULL,
    device_type VARCHAR(32) NOT NULL,
    registered BOOLEAN DEFAULT FALSE,
    deleted BOOLEAN DEFAULT FALSE,
    config_id BIGINT NULL,
    battery_level NUMERIC DEFAULT 0 CHECK ( battery_level >= 0 ), CHECK ( battery_level <= 100 ),
    address TEXT,
    description TEXT,

    CONSTRAINT registered_device_must_have_config CHECK ( NOT(registered AND config_id IS NULL) ),
    CONSTRAINT meter_unique_device_id UNIQUE (device_id),
    FOREIGN KEY (config_id) REFERENCES meter.ai_meter_config(id)
);

CREATE INDEX idx_device_id ON meter.ai_meter_device(device_id);

CREATE TABLE meter.ai_meter_subscription
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL,

    subscription_type VARCHAR(64),
    meter_id BIGINT NOT NULL,
    description TEXT,

    telegram_chat_id BIGINT,
    FOREIGN KEY (meter_id) REFERENCES meter.ai_meter_device(id)
);

CREATE TABLE IF NOT EXISTS meter.ai_meter_data
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    date_created TIMESTAMP NOT NULL,

    meter_id BIGINT NOT NULL,
    mime_type VARCHAR(255) NOT NULL,
    image_name VARCHAR(255) NOT NULL,
    image_size BIGINT NOT NULL,
    image_data BYTEA NOT NULL,
    image_date TIMESTAMP NOT NULL,
    reading NUMERIC,

    FOREIGN KEY (meter_id) REFERENCES meter.ai_meter_device(id)
);

CREATE TABLE meter.ai_meter_data_transaction
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    date_created TIMESTAMP NOT NULL,

    status VARCHAR(32) NOT NULL,
    data_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,
    message TEXT,

    FOREIGN KEY (data_id) REFERENCES meter.ai_meter_data(id),
    FOREIGN KEY (subscription_id) REFERENCES meter.ai_meter_subscription(id)
);

CREATE TABLE meter.ai_meter_token
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    date_created TIMESTAMP NOT NULL,
    
    token_type VARCHAR(64) NOT NULL,
    access_token VARCHAR(255) NOT NULL,
    meter_id BIGINT NOT NULL,
    expires_in TIMESTAMP NOT NULL,

    FOREIGN KEY (meter_id) REFERENCES meter.ai_meter_device(id)
);

CREATE INDEX idx_meter_access_token ON meter.ai_meter_token(access_token);