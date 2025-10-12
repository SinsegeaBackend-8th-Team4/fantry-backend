-- 1. product_inspection 테이블에 1차/2차 검수자 ID 컬럼 추가
ALTER TABLE product_inspection
    ADD COLUMN first_inspector_id INT NULL COMMENT '1차 검수 처리자 ID' AFTER template_version,
    ADD COLUMN second_inspector_id INT NULL COMMENT '2차 검수 처리자 ID' AFTER first_inspector_id;

-- 2. 기존 inspector_id 컬럼 삭제
ALTER TABLE product_inspection
DROP COLUMN inspector_id;