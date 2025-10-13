-- =================================================================
-- 1. 환불(Return) 도메인 DDL 리팩토링 (진짜 최종 수정안)
-- =================================================================

-- [0단계] 외부 의존성(FK) 선(先)삭제
-- `settlement_item` 테이블이 `returns` 테이블의 `return_id`를 참조하고 있으므로,
-- 컬럼 이름 변경 전에 이 외부 제약조건을 먼저 안전하게 삭제합니다.
ALTER TABLE `settlement_item` DROP FOREIGN KEY `fk_settlement_item_returns`;

-- [1단계] 테이블 이름 변경
ALTER TABLE `returns` RENAME TO `return_requests`;

-- [2단계] 내부 제약조건 선(先)삭제
ALTER TABLE `return_requests`
DROP FOREIGN KEY `fk_returns_orders`,
    DROP FOREIGN KEY `fk_returns_created_member`,
    DROP FOREIGN KEY `fk_returns_updated_member`,
    DROP FOREIGN KEY `fk_returns_member`;

-- [3단계] 컬럼 구조 변경
-- 이제 모든 내/외부 제약조건이 제거되었으므로, MySQL은 아무런 충돌 없이 이 작업을 안전하게 수행합니다.
ALTER TABLE `return_requests`
    CHANGE `return_id` `return_request_id` INT NOT NULL AUTO_INCREMENT COMMENT 'PK',
    MODIFY COLUMN `status` ENUM(
    'REQUESTED', 'IN_TRANSIT', 'INSPECTING', 'APPROVED', 'REJECTED', 'COMPLETED'
    ) NOT NULL DEFAULT 'REQUESTED' COMMENT '환불 상태',
    ADD COLUMN `original_payment_amount` DECIMAL(15,2) NOT NULL COMMENT '원본 결제 금액' AFTER `order_id`,
    ADD COLUMN `deducted_shipping_fee` DECIMAL(15,2) DEFAULT 0.00 COMMENT '차감된 배송비' AFTER `original_payment_amount`,
    CHANGE `return_amount` `final_refund_amount` DECIMAL(15,2) NOT NULL COMMENT '최종 환불 금액';

-- [4단계] 내부 제약조건 재생성
ALTER TABLE `return_requests`
    ADD CONSTRAINT `fk_return_request_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`orders_id`),
    ADD CONSTRAINT `fk_return_request_created_by` FOREIGN KEY (`created_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
    ADD CONSTRAINT `fk_return_request_updated_by` FOREIGN KEY (`updated_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
    ADD CONSTRAINT `fk_return_request_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE SET NULL;

-- [5단계] 외부 의존성(FK) 재생성
-- 모든 작업이 끝난 후, `settlement_item` 테이블이 새로운 테이블/컬럼 이름을 참조하도록 관계를 다시 설정합니다.
ALTER TABLE `settlement_item`
    ADD CONSTRAINT `fk_settlement_item_return_request`
        FOREIGN KEY (`return_id`) REFERENCES `return_requests` (`return_request_id`);


-- =================================================================
-- 2. 정산(Settlement) 도메인 DDL (신규)
-- =================================================================
CREATE TABLE `revenue_ledger` (
                                  `ledger_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                  `transaction_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '거래 발생 시간',
                                  `revenue_type` ENUM(
      'COMMISSION', 'DIRECT_PURCHASE_PROFIT', 'CANCELLATION_FEE', 'REFUND_DEDUCTION', 'REFUND_LOSS'
  ) NOT NULL COMMENT '수익/비용 유형',
                                  `amount` DECIMAL(15,2) NOT NULL COMMENT '금액 (수익: +, 비용: -)',
                                  `related_order_id` INT NULL COMMENT '관련 주문 ID',
                                  `related_return_id` INT NULL COMMENT '관련 환불 ID',
                                  `related_settlement_id` INT NULL COMMENT '관련 정산 ID',
                                  `description` VARCHAR(255) NULL COMMENT '거래 내용 요약',
                                  PRIMARY KEY (`ledger_id`),
                                  INDEX `idx_revenue_ledger_type_date` (`revenue_type`, `transaction_at`),
                                  CONSTRAINT `fk_revenue_ledger_order` FOREIGN KEY (`related_order_id`) REFERENCES `orders` (`orders_id`),
                                  CONSTRAINT `fk_revenue_ledger_return` FOREIGN KEY (`related_return_id`) REFERENCES `return_requests` (`return_request_id`),
                                  CONSTRAINT `fk_revenue_ledger_settlement` FOREIGN KEY (`related_settlement_id`) REFERENCES `settlement` (`settlement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='통합 매출 장부';

