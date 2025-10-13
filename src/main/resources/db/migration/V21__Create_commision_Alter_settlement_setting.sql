-- fantry_my.commission_rule definition

CREATE TABLE `commission_rule` (
                                   `rule_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                   `rule_name` varchar(100) NOT NULL COMMENT '규칙 이름 (예: 포토카드 카테고리 수수료)',
                                   `commission_rate` decimal(5,2) NOT NULL COMMENT '이 규칙에 해당하는 수수료율',
                                   `condition_type` enum('CATEGORY','USER_GRADE') NOT NULL COMMENT '규칙 조건 유형',
                                   `condition_value` varchar(100) NOT NULL COMMENT '규칙 조건의 값 (예: PC, VIP)',
                                   `priority` int NOT NULL DEFAULT '0' COMMENT '우선순위 (숫자가 높을수록 먼저 적용)',
                                   `start_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '규칙 시작일',
                                   `end_date` datetime DEFAULT NULL COMMENT '규칙 종료일 (NULL이면 무기한)',
                                   `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '규칙 활성화 여부',
                                   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
                                   `created_by` int DEFAULT NULL COMMENT '생성자',
                                   `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
                                   `updated_by` int DEFAULT NULL COMMENT '수정자',
                                   PRIMARY KEY (`rule_id`),
                                   KEY `fk_commission_rule_created_by` (`created_by`),
                                   KEY `fk_commission_rule_updated_by` (`updated_by`),
                                   CONSTRAINT `fk_commission_rule_created_by` FOREIGN KEY (`created_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                                   CONSTRAINT `fk_commission_rule_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='수수료 정책 규칙 테이블';

-- 1. 기존 컬럼 삭제

ALTER TABLE `settlement_setting`
DROP COLUMN `settlement_cycle_days`,
DROP COLUMN `settlement_schedule`;

-- 2. 신규 컬럼 추가
ALTER TABLE `settlement_setting`
    ADD COLUMN `settlement_cycle_type` ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'END_OF_MONTH') NOT NULL COMMENT '정산 주기 유형' AFTER `commission_rate`,
ADD COLUMN `settlement_day` INT DEFAULT NULL COMMENT '정산 주기 상세일 (주: 1-7, 월: 1-31)' AFTER `settlement_cycle_type`;