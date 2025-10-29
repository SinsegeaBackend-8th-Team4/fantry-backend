
CREATE TABLE `return_status_history` (
                                         `history_id` INT NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                         `return_request_id` INT NOT NULL COMMENT 'FK to return_requests table',
                                         `previous_status` VARCHAR(50) NULL COMMENT '변경 전 상태',
                                         `new_status` VARCHAR(50) NOT NULL COMMENT '변경 후 상태',
                                         `updated_by` INT NULL COMMENT '상태 변경 처리자 (member_id)',
                                         `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '상태 변경 시간',
                                         `memo` VARCHAR(255) NULL COMMENT '상태 변경 관련 메모',
                                         PRIMARY KEY (`history_id`),
                                         INDEX `idx_return_status_history_request_id` (`return_request_id`),
                                         CONSTRAINT `fk_return_status_history_return_request` FOREIGN KEY (`return_request_id`) REFERENCES `return_requests` (`return_request_id`),
                                         CONSTRAINT `fk_return_status_history_member` FOREIGN KEY (`updated_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='환불 요청 상태 변경 이력';