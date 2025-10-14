-- 1. '사유 종류'를 저장할 ENUM 타입의 reason 컬럼을 추가합니다.
ALTER TABLE `return_requests`
    ADD COLUMN `reason` ENUM('SIMPLE_CHANGE_OF_MIND', 'PRODUCT_DEFECT', 'SHIPPING_ERROR', 'ETC') NOT NULL COMMENT '환불/반품 사유 종류' AFTER `final_refund_amount`;

-- 2. '상세 사유'를 저장하는 기존 return_reason 컬럼의 이름을 detail_reason으로 변경합니다.
ALTER TABLE `return_requests`
    CHANGE COLUMN `return_reason` `detail_reason` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '상세 환불/반품 사유';