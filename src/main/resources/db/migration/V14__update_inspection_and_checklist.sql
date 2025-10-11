-- =======================================
-- 1. product_inspection 테이블에 1차/2차 반려 사유 컬럼 추가
-- 2. 기존 상태값을 새로운 ENUM 값 체계에 맞게 업데이트
-- 3. inspection_status ENUM 수정
-- 4. product_checklist_answer 테이블에 note 컬럼 추가
-- =======================================

-- 1. product_inspection 테이블 컬럼 추가
ALTER TABLE product_inspection
    ADD COLUMN first_rejection_reason VARCHAR(255) NULL AFTER updated_at,
    ADD COLUMN second_rejection_reason VARCHAR(255) NULL AFTER first_rejection_reason;

-- 2. 기존 상태값 매핑 업데이트
UPDATE product_inspection
SET inspection_status = 'ONLINE_APPROVED'
WHERE inspection_status = 'FIRST_REVIEWED';

UPDATE product_inspection
SET inspection_status = 'ONLINE_REJECTED'
WHERE inspection_status = 'FIRST_REJECTED';

UPDATE product_inspection
SET inspection_status = 'OFFLINE_REJECTED'
WHERE inspection_status = 'SECOND_REJECTED';

-- 3. inspection_status ENUM 수정
ALTER TABLE product_inspection
    MODIFY COLUMN inspection_status ENUM (
    'DRAFT',
    'SUBMITTED',
    'ONLINE_APPROVED',
    'ONLINE_REJECTED',
    'OFFLINE_INSPECTING',
    'OFFLINE_REJECTED',
    'COMPLETED'
    ) NOT NULL DEFAULT 'DRAFT';

-- 4. product_checklist_answer 테이블에 note 컬럼 추가
ALTER TABLE product_checklist_answer
    ADD COLUMN note VARCHAR(255) NULL AFTER answer_value;
