-- 1. artist 테이블에 status 컬럼 추가
-- ENUM 타입을 사용하여 데이터의 정합성을 보장합니다.
-- PENDING: 사용자가 추가한 후 관리자 승인을 기다리는 상태 (새로 추가될 때의 기본값)
-- APPROVED: 관리자가 승인한 공식 데이터
-- REJECTED: 부적절한 데이터로 확인되어 반려된 상태
ALTER TABLE artist
ADD COLUMN `status` ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING' COMMENT '아티스트 데이터 상태';


-- 2. 기존에 있던 모든 아티스트 데이터들은 'APPROVED' (승인된) 상태로 업데이트
-- 이 마이그레이션 실행 시점의 데이터는 모두 공식 데이터로 간주합니다.
UPDATE artist
SET status = 'APPROVED';
