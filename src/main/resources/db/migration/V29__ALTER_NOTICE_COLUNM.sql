-- === Notice 도메인 분리 마이그레이션 ===

-- Step 1: '공지사항 유형'을 관리할 `notice_type` 테이블을 새로 생성합니다.
CREATE TABLE `notice_type` (
                               `notice_type_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                               `name` varchar(50) NOT NULL COMMENT '공지유형(공지, 이벤트 등)',
                               PRIMARY KEY (`notice_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='공지사항 유형';

-- Step 2: 새로 만든 `notice_type` 테이블에 기본 데이터를 추가합니다. (공지, 이벤트)
INSERT INTO `notice_type` (`name`) VALUES ('공지'), ('이벤트');

-- Step 3: 기존 `notice` 테이블에 새로운 FK 컬럼인 `notice_type_id`를 추가합니다. (아직은 NULL 허용)
ALTER TABLE `notice`
    ADD COLUMN `notice_type_id` INT NULL COMMENT 'FK 공지유형ID' AFTER `cs_type_id`;

-- Step 4: [데이터 마이그레이션] 기존 공지사항들의 유형을 '공지'로 일괄 지정합니다.
-- (만약 다른 규칙이 필요하다면 이 UPDATE 문을 수정해야 합니다.)
UPDATE `notice` SET `notice_type_id` = 1;

-- Step 5: 데이터 마이그레이션이 끝났으므로, 이제 `notice_type_id` 컬럼에 NOT NULL 제약조건을 추가합니다.
ALTER TABLE `notice`
    MODIFY COLUMN `notice_type_id` INT NOT NULL;

-- Step 6: 더 이상 사용하지 않을 `cs_type`과의 외래 키 제약조건을 삭제합니다.
ALTER TABLE `notice`
DROP FOREIGN KEY `fk_notice_cs_type`;

-- Step 7: `notice_type` 테이블과의 새로운 외래 키 제약조건을 추가합니다.
ALTER TABLE `notice`
    ADD CONSTRAINT `fk_notice_notice_type` FOREIGN KEY (`notice_type_id`) REFERENCES `notice_type` (`notice_type_id`);

-- Step 8: 마지막으로, 이제는 필요 없어진 `cs_type_id` 컬럼을 완전히 삭제합니다.
ALTER TABLE `notice`
DROP COLUMN `cs_type_id`;