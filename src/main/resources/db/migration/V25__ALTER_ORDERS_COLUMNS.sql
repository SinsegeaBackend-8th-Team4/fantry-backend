-- `orders` 테이블의 `delivered_at`과 `cancelled_at` 컬럼 추가

ALTER TABLE `orders`
    ADD COLUMN `delivered_at` DATETIME NULL COMMENT '배송 완료 일시' AFTER `updated_at`,
    ADD COLUMN `cancelled_at` DATETIME NULL COMMENT '주문 취소 일시' AFTER `delivered_at`;
