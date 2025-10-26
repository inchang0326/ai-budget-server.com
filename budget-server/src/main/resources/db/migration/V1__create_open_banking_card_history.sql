CREATE TABLE open_banking_card_history
(
    user_id             VARCHAR(20)      NOT NULL,
    card_no             VARCHAR(16)      NOT NULL,
    timestamp           VARCHAR(17)      NOT NULL,
    card_company_code   VARCHAR(4)       NOT NULL,
    type                VARCHAR(20)      NOT NULL,
    amount              DOUBLE PRECISION NOT NULL,
    category            VARCHAR(100)     NOT NULL,
    description         VARCHAR(200)     NOT NULL,
    created_time        TIMESTAMP(0)     NOT NULL,
    -- 복합 PK 정의 (순서 지정)
    PRIMARY KEY (user_id, card_no, timestamp)
);