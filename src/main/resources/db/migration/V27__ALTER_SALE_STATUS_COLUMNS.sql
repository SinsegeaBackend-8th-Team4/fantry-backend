-- auction 테이블의 sale_status ENUM 값 변경 (REACTIVE 추가)

ALTER TABLE auction
    MODIFY COLUMN sale_status ENUM(
    'PREPARING',  -- 판매대기
    'ACTIVE',     -- 판매중
    'SOLD',       -- 판매 완료
    'NOT_SOLD',   -- 미판매
    'CANCELED',   -- 판매취소
    'REPREPARING', -- 재준비중
    'REACTIVE'    -- 재판매중
    ) NOT NULL DEFAULT 'PREPARING';