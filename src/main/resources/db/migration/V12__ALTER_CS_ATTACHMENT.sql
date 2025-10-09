ALTER TABLE `cs_attachment` RENAME TO `inquiry_attachment`;

-- 1-2. Primary Key 컬럼 이름을 테이블에 맞게 수정합니다.
ALTER TABLE `inquiry_attachment` CHANGE `cs_attachment_id` `inquiry_attachment_id` INT NOT NULL AUTO_INCREMENT COMMENT 'PK';

-- 1-3. 기존의 모호했던 제약조건들을 삭제합니다.
ALTER TABLE `inquiry_attachment` DROP FOREIGN KEY `fk_cs_attachement_inquiry`;
ALTER TABLE `inquiry_attachment` DROP FOREIGN KEY `fk_cs_attachement_filemeta`;

-- 1-4. 새롭고 명확한 이름으로 제약조건을 다시 추가합니다.
ALTER TABLE `inquiry_attachment` ADD CONSTRAINT `fk_inquiry_attachment_inquiry`
    FOREIGN KEY (`inquiry_id`) REFERENCES `inquiry` (`inquiry_id`);

ALTER TABLE `inquiry_attachment` ADD CONSTRAINT `fk_inquiry_attachment_filemeta`
    FOREIGN KEY (`filemeta_id`) REFERENCES `filemeta` (`filemeta_id`);