CREATE TABLE meter.ai_meter_config
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL,

    device_name VARCHAR(255),
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

CREATE TABLE meter.ai_meter_integration
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL,

    type VARCHAR(32),
    device_id UUID NOT NULL,
    description TEXT,

    telegram_chat_id BIGINT,
    FOREIGN KEY (device_id) REFERENCES meter.ai_meter_device(device_id)
);

CREATE TABLE IF NOT EXISTS meter.ai_meter_data
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL,

    device_id UUID NOT NULL,
    mime_type CHARACTER VARYING(255) NOT NULL,
    image_name CHARACTER VARYING(255) NOT NULL,
    image_size BIGINT NOT NULL,
    image_data BYTEA NOT NULL,
    reading NUMERIC,

    FOREIGN KEY (device_id) REFERENCES meter.ai_meter_device(device_id)
);

CREATE INDEX idx_meter_data_device_id ON meter.ai_meter_data(device_id);

CREATE TABLE meter.ai_meter_data_transaction
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,

    status VARCHAR(32) NOT NULL,
    data_id SERIAL NOT NULL,
    integration_id SERIAL NOT NULL,

    FOREIGN KEY (data_id) REFERENCES meter.ai_meter_data(id),
    FOREIGN KEY (integration_id) REFERENCES meter.ai_meter_integration(id)
);