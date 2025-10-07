-- 기존 데이터에 'REJECTED'가 있다면 'FIRST_REJECTED'로 먼저 변경합니다.
UPDATE product_inspection
SET inspection_status = 'FIRST_REJECTED'
WHERE inspection_status = 'REJECTED';

-- inspection_status 컬럼의 ENUM 정의를 변경합니다.
ALTER TABLE product_inspection
    MODIFY COLUMN inspection_status ENUM (
    'DRAFT',
    'SUBMITTED',
    'FIRST_REVIEWED',
    'FIRST_REJECTED',
    'OFFLINE_INSPECTING',
    'SECOND_REJECTED',
    'COMPLETED'
    ) NOT NULL DEFAULT 'DRAFT';