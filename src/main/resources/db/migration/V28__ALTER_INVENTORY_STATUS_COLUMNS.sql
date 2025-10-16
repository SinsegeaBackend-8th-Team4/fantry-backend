-- product_inspection 의 inventory_status ENUM값 변경 , 등록대기 , 미판매 , 재판매중 추가

ALTER TABLE product_inspection
    MODIFY COLUMN inventory_status ENUM(
    'PENDING_REGIST',  -- 판매등록대기
    'PENDING_ACTIVE',  -- 판매대기
    'ACTIVE',          -- 판매중
    'SOLD',            -- 판매 완료
    'NOT_SOLD',        -- 미판매
    'CANCELED',        -- 판매취소
    'REACTIVE'         -- 재판매중
    ) NOT NULL DEFAULT 'PENDING_REGIST';