CREATE TABLE articles (
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(255),
    summary    VARCHAR(255),
    content    TEXT,
    tags       TEXT,
    published  BOOLEAN,
    created_at TIMESTAMP WITH TIME ZONE
);
