ALTER TABLE `inquiry` DROP FOREIGN KEY `fk_inquiry_create_member`;

ALTER TABLE `inquiry`
    CHANGE `created_at` `inquired_at` DATETIME(6) NOT NULL COMMENT '문의글 작성 시간',
    CHANGE `created_by` `inquired_by` INT NULL COMMENT 'FK 문의자(멤버ID)';

ALTER TABLE `inquiry`
    ADD CONSTRAINT `fk_inquiry_inquired_by_member`
        FOREIGN KEY (`inquired_by`)
            REFERENCES `member` (`member_id`)
            ON DELETE SET NULL;