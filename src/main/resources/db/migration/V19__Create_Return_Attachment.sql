-- fantry_my.return_attachment definition

CREATE TABLE `return_attachment` (
                                     `return_attachment_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                     `return_request_id` int NOT NULL COMMENT 'FK to return_requests table',
                                     `filemeta_id` int NOT NULL COMMENT 'FK to filemeta table',
                                     PRIMARY KEY (`return_attachment_id`),
                                     KEY `fk_return_attachment_return_request` (`return_request_id`),
                                     KEY `fk_return_attachment_filemeta` (`filemeta_id`),
                                     CONSTRAINT `fk_return_attachment_filemeta` FOREIGN KEY (`filemeta_id`) REFERENCES `filemeta` (`filemeta_id`),
                                     CONSTRAINT `fk_return_attachment_return_request` FOREIGN KEY (`return_request_id`) REFERENCES `return_requests` (`return_request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='환불/반품 요청 첨부파일 관계 테이블';