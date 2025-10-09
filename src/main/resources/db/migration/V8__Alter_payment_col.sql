ALTER TABLE payment MODIFY COLUMN status VARCHAR(30) NOT NULL;
ALTER TABLE payment ADD bootpay_status varchar(30) NULL;
ALTER TABLE payment ADD updated_at TIMESTAMP NULL;
