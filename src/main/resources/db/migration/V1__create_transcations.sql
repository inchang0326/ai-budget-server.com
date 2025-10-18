CREATE TABLE transactions
(
    user_id      VARCHAR(20)      NOT NULL,
    timestamp    VARCHAR(17)      NOT NULL,
    type         VARCHAR(20)      NOT NULL,
    amount       DOUBLE PRECISION NOT NULL,
    category     VARCHAR(100)     NOT NULL,
    description  VARCHAR(200)     NOT NULL,
    updated_time TIMESTAMP        NOT NULL,
    -- 복합 PK 정의 (순서 지정)
    PRIMARY KEY (user_id, timestamp)
);