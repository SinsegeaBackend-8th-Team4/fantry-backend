CREATE TABLE `faq_attachment` (
                                  `faq_attachment_id` INT NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                  `faq_id` INT NULL COMMENT 'FK to faq table',
                                  `filemeta_id` INT NULL COMMENT 'FK to filemeta table',
                                  PRIMARY KEY (`faq_attachment_id`),
                                  CONSTRAINT `fk_faq_attachment_faq` FOREIGN KEY (`faq_id`) REFERENCES `faq` (`faq_id`),
                                  CONSTRAINT `fk_faq_attachment_filemeta` FOREIGN KEY (`filemeta_id`) REFERENCES `filemeta` (`filemeta_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='FAQ 첨부파일 관계 테이블';

CREATE TABLE `notice_attachment` (
                                     `notice_attachment_id` INT NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                     `notice_id` INT NULL COMMENT 'FK to notice table',
                                     `filemeta_id` INT NULL COMMENT 'FK to filemeta table',
                                     PRIMARY KEY (`notice_attachment_id`),
                                     CONSTRAINT `fk_notice_attachment_notice` FOREIGN KEY (`notice_id`) REFERENCES `notice` (`notice_id`),
                                     CONSTRAINT `fk_notice_attachment_filemeta` FOREIGN KEY (`filemeta_id`) REFERENCES `filemeta` (`filemeta_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='공지사항 첨부파일 관계 테이블';
