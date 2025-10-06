ALTER TABLE payment ADD bootpay_status varchar(30) NULL;
ALTER TABLE payment MODIFY COLUMN status varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL;
ALTER TABLE payment ADD updated_at TIMESTAMP NULL;
