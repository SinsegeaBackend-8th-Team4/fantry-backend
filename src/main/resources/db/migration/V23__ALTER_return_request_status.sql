-- `return_requests` 테이블의 `status` 컬럼 정의를 최신화하여,
-- 누락된 'USER_CANCELLED'와 'DELETED'를 ENUM 목록에 추가합니다.
ALTER TABLE `return_requests`
    MODIFY COLUMN `status` ENUM('REQUESTED', 'IN_TRANSIT', 'INSPECTING', 'APPROVED', 'REJECTED', 'COMPLETED', 'USER_CANCELLED', 'DELETED') NOT NULL DEFAULT 'REQUESTED' COMMENT '환불 상태';