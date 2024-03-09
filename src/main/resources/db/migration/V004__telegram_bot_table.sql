CREATE TABLE meter.ai_meter_bot_chat
(
    id SERIAL PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    date_created TIMESTAMP NOT NULL,

    chat_type VARCHAR(64) NOT NULL,
    chat_id BIGINT NOT NULL,
    user_language VARCHAR(16) NOT NULL
);

CREATE INDEX idx_bot_chat_id ON meter.ai_meter_bot_chat(chat_id);