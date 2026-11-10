CREATE TABLE IF NOT EXISTS chat_session (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS chat_message (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    role VARCHAR(20) NOT NULL,
    content LONGTEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_chat_message_session FOREIGN KEY (session_id)
        REFERENCES chat_session(id) ON DELETE CASCADE,
    INDEX idx_chat_message_session_created(session_id, created_at, id)
);
