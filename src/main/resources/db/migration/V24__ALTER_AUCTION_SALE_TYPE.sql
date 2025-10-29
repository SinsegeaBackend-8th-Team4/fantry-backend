-- sale_type 의 tinyint 를 ENUM 으로 변경
ALTER TABLE auction
    MODIFY COLUMN sale_type ENUM('AUCTION','INSTANT_BUY') NOT NULL;