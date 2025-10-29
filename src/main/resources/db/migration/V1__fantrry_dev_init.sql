-- fantry_dev.artist definition

CREATE TABLE `artist` (
                          `artist_id` int NOT NULL AUTO_INCREMENT COMMENT '아티스트 ID',
                          `name_ko` varchar(100) NOT NULL COMMENT '한글명',
                          `name_en` varchar(100) NOT NULL COMMENT '영문명',
                          `group_type` enum('MALE_GROUP','FEMALE_GROUP','MALE_SOLO','FEMALE_SOLO','MIXED','OTHER') NOT NULL COMMENT '그룹 구분',
                          `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`artist_id`),
                          UNIQUE KEY `name_ko` (`name_ko`),
                          UNIQUE KEY `name_en` (`name_en`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='아티스트';


-- fantry_dev.auction definition

CREATE TABLE `auction` (
                           `auction_id` int NOT NULL AUTO_INCREMENT,
                           `sale_type` tinyint NOT NULL COMMENT '0: AUCTION, 1: INSTANT_BUY',
                           `sale_status` enum('PREPARING','ACTIVE','SOLD','NOT_SOLD','CANCELLED') NOT NULL DEFAULT 'PREPARING',
                           `start_price` int NOT NULL COMMENT '경매 시작가 또는 즉시 구매가',
                           `final_price` int DEFAULT NULL COMMENT '최종 판매가',
                           `start_time` datetime NOT NULL COMMENT '판매/경매 시작 시간',
                           `end_time` datetime NOT NULL COMMENT '판매/경매 마감 시간',
                           `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           `inventory_item_id` int DEFAULT NULL,
                           PRIMARY KEY (`auction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.bid definition

CREATE TABLE `bid` (
                       `bid_id` int NOT NULL AUTO_INCREMENT,
                       `bid_amount` int NOT NULL,
                       `bid_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       `bidder_name` varchar(20) NOT NULL,
                       `bidder_id` varchar(20) NOT NULL,
                       `item_name` varchar(100) NOT NULL,
                       PRIMARY KEY (`bid_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.checklist_template definition

CREATE TABLE `checklist_template` (
                                      `checklist_template_id` int NOT NULL AUTO_INCREMENT,
                                      `code` varchar(64) NOT NULL,
                                      `role` enum('SELLER','INSPECTOR') NOT NULL,
                                      `version` int NOT NULL,
                                      `title` varchar(200) DEFAULT NULL,
                                      `status` enum('DRAFT','PUBLISHED','ARCHIVED') NOT NULL,
                                      `published_at` datetime DEFAULT NULL,
                                      `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                      `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      PRIMARY KEY (`checklist_template_id`),
                                      UNIQUE KEY `uk_template_code_role_version` (`code`,`role`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.cs_type definition

CREATE TABLE `cs_type` (
                           `cs_type_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                           `name` varchar(50) NOT NULL COMMENT '문의유형(상품문의, 배송문의 등)',
                           PRIMARY KEY (`cs_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.goods_category definition

CREATE TABLE `goods_category` (
                                  `goods_category_id` int NOT NULL AUTO_INCREMENT COMMENT '카테고리 ID',
                                  `code` varchar(64) NOT NULL COMMENT '코드 (PC, ALBUM, DVD, SEASON_GREETING, LIGHTSTICK, ETC)',
                                  `name` varchar(100) NOT NULL COMMENT '카테고리명',
                                  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
                                  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
                                  PRIMARY KEY (`goods_category_id`),
                                  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='굿즈 카테고리';


-- fantry_dev.orders definition

CREATE TABLE `orders` (
                          `orders_id` int NOT NULL AUTO_INCREMENT,
                          `price` int NOT NULL,
                          `shipping_address` varchar(50) DEFAULT NULL,
                          `order_status` enum('PENDING_PAYMENT','PAID','PREPARING_SHIPMENT','SHIPPED','DELIVERED','CONFIRMED','CANCEL_REQUESTED','CANCELLED','REFUND_REQUESTED','REFUNDED') DEFAULT 'PENDING_PAYMENT',
                          `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          `auction_id` int DEFAULT NULL,
                          `buyer_id` int DEFAULT NULL,
                          `payment_id` int DEFAULT NULL,
                          PRIMARY KEY (`orders_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.payment definition

CREATE TABLE `payment` (
                           `payment_id` int NOT NULL AUTO_INCREMENT,
                           `receipt_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `order_id` varchar(64) NOT NULL,
                           `price` int NOT NULL,
                           `cancelled_price` int DEFAULT NULL,
                           `order_name` varchar(50) DEFAULT NULL,
                           `metadata` varchar(100) DEFAULT NULL,
                           `pg` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `currency` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `status` tinyint DEFAULT NULL,
                           `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `requested_at` timestamp NULL DEFAULT NULL,
                           `purchased_at` timestamp NULL DEFAULT NULL,
                           `cancelled_at` timestamp NULL DEFAULT NULL,
                           `receipt_url` text,
                           `payment_info` json DEFAULT (json_object()),
                           `version` bigint NOT NULL DEFAULT '0',
                           PRIMARY KEY (`payment_id`),
                           UNIQUE KEY `order_id` (`order_id`),
                           UNIQUE KEY `order_id_2` (`order_id`),
                           UNIQUE KEY `receipt_id` (`receipt_id`),
                           KEY `idx_payment_receipt_id` (`receipt_id`),
                           KEY `idx_payment_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.`role` definition

CREATE TABLE `role` (
                        `role_id` int NOT NULL AUTO_INCREMENT,
                        `role_type` enum('SADMIN','ADMIN','USER','RUSER') DEFAULT NULL COMMENT '권한종류: SADMIN: 슈퍼 관리자, ADMIN: 관리자, USER: 일반 회원, RUSER: 제한된 회원',
                        PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fantry_dev.album definition

CREATE TABLE `album` (
                         `album_id` int NOT NULL AUTO_INCREMENT COMMENT '앨범 ID',
                         `artist_id` int NOT NULL COMMENT '아티스트 ID',
                         `title` varchar(200) NOT NULL COMMENT '앨범명',
                         `release_date` date NOT NULL COMMENT '발매일',
                         `version` varchar(100) DEFAULT NULL COMMENT '버전',
                         `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                         `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (`album_id`),
                         KEY `fk_album_artist` (`artist_id`),
                         CONSTRAINT `fk_album_artist` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`artist_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='앨범';


-- fantry_dev.checklist_item definition

CREATE TABLE `checklist_item` (
                                  `checklist_item_id` int NOT NULL AUTO_INCREMENT,
                                  `checklist_template_id` int NOT NULL,
                                  `item_key` varchar(128) NOT NULL,
                                  `label` varchar(256) NOT NULL,
                                  `type` enum('BOOL','SELECT','MULTISELECT','TEXT','NUMBER','GRADE','PHOTO') NOT NULL,
                                  `options` json DEFAULT NULL,
                                  `required` tinyint(1) NOT NULL DEFAULT '0',
                                  `order_index` int DEFAULT NULL,
                                  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`checklist_item_id`),
                                  KEY `fk_checklist_item_template` (`checklist_template_id`),
                                  CONSTRAINT `fk_checklist_item_template` FOREIGN KEY (`checklist_template_id`) REFERENCES `checklist_template` (`checklist_template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.`member` definition

CREATE TABLE `member` (
                          `member_id` int NOT NULL AUTO_INCREMENT,
                          `id` varchar(20) NOT NULL,
                          `password` varchar(64) DEFAULT NULL,
                          `name` varchar(30) DEFAULT NULL,
                          `email` varchar(150) DEFAULT NULL,
                          `tel` varchar(20) DEFAULT NULL,
                          `sns` varchar(10) DEFAULT NULL,
                          `create_at` datetime DEFAULT CURRENT_TIMESTAMP,
                          `leaved_at` datetime DEFAULT NULL,
                          `is_active` tinyint(1) DEFAULT '0',
                          `role_id` int NOT NULL,
                          PRIMARY KEY (`member_id`),
                          UNIQUE KEY `id` (`id`),
                          UNIQUE KEY `email` (`email`),
                          KEY `fk_member_role` (`role_id`),
                          CONSTRAINT `fk_member_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.price_baseline definition

CREATE TABLE `price_baseline` (
                                  `price_baseline_id` int NOT NULL AUTO_INCREMENT,
                                  `goods_category_id` int NOT NULL,
                                  `source` enum('SEED','MSRP_BASE','MARKET_AVG') NOT NULL,
                                  `amount` decimal(12,2) NOT NULL,
                                  `effective_at` datetime NOT NULL,
                                  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`price_baseline_id`),
                                  KEY `fk_price_baseline_category` (`goods_category_id`),
                                  CONSTRAINT `fk_price_baseline_category` FOREIGN KEY (`goods_category_id`) REFERENCES `goods_category` (`goods_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.pricing_rule definition

CREATE TABLE `pricing_rule` (
                                `pricing_rule_id` int NOT NULL AUTO_INCREMENT,
                                `goods_category_id` int NOT NULL,
                                `item_key` varchar(128) NOT NULL,
                                `option_value` varchar(128) NOT NULL,
                                `effective_type` enum('PCT','ABS','CAP') NOT NULL,
                                `pct_value` decimal(6,3) DEFAULT NULL,
                                `abs_value` decimal(12,3) DEFAULT NULL,
                                `cap_min_multiplier` decimal(6,3) DEFAULT NULL,
                                `active` tinyint(1) NOT NULL DEFAULT '1',
                                `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`pricing_rule_id`),
                                KEY `fk_pricing_rule_category` (`goods_category_id`),
                                CONSTRAINT `fk_pricing_rule_category` FOREIGN KEY (`goods_category_id`) REFERENCES `goods_category` (`goods_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fantry_dev.checklist_item_category_map definition

CREATE TABLE `checklist_item_category_map` (
                                               `checklist_item_id` int NOT NULL,
                                               `goods_category_id` int NOT NULL,
                                               PRIMARY KEY (`checklist_item_id`,`goods_category_id`),
                                               KEY `fk_cicm_category` (`goods_category_id`),
                                               CONSTRAINT `fk_cicm_category` FOREIGN KEY (`goods_category_id`) REFERENCES `goods_category` (`goods_category_id`),
                                               CONSTRAINT `fk_cicm_item` FOREIGN KEY (`checklist_item_id`) REFERENCES `checklist_item` (`checklist_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.notice definition

CREATE TABLE `notice` (
                          `notice_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                          `cs_type_id` int NOT NULL COMMENT 'FK 문의유형ID',
                          `title` varchar(50) NOT NULL COMMENT '문의 제목',
                          `content` mediumtext NOT NULL COMMENT '문의 본문',
                          `status` enum('DRAFT','ACTIVE','PINNED','INACTIVE') NOT NULL COMMENT '임시저장, 활성화(노출), 상단노출, 비활성화(노출안함)',
                          `created_by` int DEFAULT NULL COMMENT '작성자',
                          `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성시간',
                          `updated_by` int DEFAULT NULL COMMENT '수정자',
                          `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정시간',
                          PRIMARY KEY (`notice_id`),
                          KEY `fk_notice_cs_type` (`cs_type_id`),
                          KEY `fk_notice_create_member` (`created_by`),
                          KEY `fk_notice_update_member` (`updated_by`),
                          CONSTRAINT `fk_notice_create_member` FOREIGN KEY (`created_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                          CONSTRAINT `fk_notice_cs_type` FOREIGN KEY (`cs_type_id`) REFERENCES `cs_type` (`cs_type_id`),
                          CONSTRAINT `fk_notice_update_member` FOREIGN KEY (`updated_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.product_inspection definition

CREATE TABLE `product_inspection` (
                                      `product_inspection_id` int NOT NULL AUTO_INCREMENT COMMENT '상품검수 ID',
                                      `submission_uuid` char(36) NOT NULL COMMENT '제출 UUID',
                                      `member_id` int NOT NULL COMMENT '작성자 ID',
                                      `template_id` int DEFAULT NULL COMMENT '체크리스트 템플릿 ID',
                                      `template_version` int DEFAULT NULL COMMENT '템플릿 버전',
                                      `goods_category_id` int NOT NULL COMMENT '카테고리 ID',
                                      `artist_id` int NOT NULL COMMENT '아티스트 ID',
                                      `album_id` int DEFAULT NULL COMMENT '앨범 ID',
                                      `item_name` varchar(255) DEFAULT NULL COMMENT '상품명',
                                      `item_description` text COMMENT '상품 설명',
                                      `hashtags` varchar(500) DEFAULT NULL COMMENT '해시태그',
                                      `expected_price` decimal(12,2) DEFAULT NULL,
                                      `market_avg_price` decimal(12,2) DEFAULT NULL,
                                      `seller_hope_price` decimal(12,2) DEFAULT NULL,
                                      `final_buy_price` decimal(12,2) DEFAULT NULL,
                                      `shipping_address` varchar(255) DEFAULT NULL,
                                      `shipping_address_detail` varchar(255) DEFAULT NULL,
                                      `bank_name` varchar(100) DEFAULT NULL,
                                      `bank_account` varchar(60) DEFAULT NULL,
                                      `inspection_status` enum('DRAFT','SUBMITTED','FIRST_REVIEWED','OFFLINE_INSPECTING','COMPLETED','REJECTED') NOT NULL DEFAULT 'DRAFT',
                                      `inventory_status` enum('PENDING','ACTIVE','SOLD','CANCELED') NOT NULL DEFAULT 'PENDING',
                                      `source_type` enum('CONSIGNMENT','PURCHASED') DEFAULT NULL,
                                      `inspector_id` int DEFAULT NULL,
                                      `final_grade` enum('A','B','C') DEFAULT NULL,
                                      `inspection_summary` json DEFAULT NULL,
                                      `price_deduction_reason` text,
                                      `inspection_notes` text,
                                      `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                      `submitted_at` datetime DEFAULT NULL,
                                      `online_inspected_at` datetime DEFAULT NULL,
                                      `offline_inspected_at` datetime DEFAULT NULL,
                                      `completed_at` datetime DEFAULT NULL,
                                      `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      PRIMARY KEY (`product_inspection_id`),
                                      UNIQUE KEY `submission_uuid` (`submission_uuid`),
                                      KEY `fk_pi_category` (`goods_category_id`),
                                      KEY `fk_pi_artist` (`artist_id`),
                                      KEY `fk_pi_album` (`album_id`),
                                      CONSTRAINT `fk_pi_album` FOREIGN KEY (`album_id`) REFERENCES `album` (`album_id`),
                                      CONSTRAINT `fk_pi_artist` FOREIGN KEY (`artist_id`) REFERENCES `artist` (`artist_id`),
                                      CONSTRAINT `fk_pi_category` FOREIGN KEY (`goods_category_id`) REFERENCES `goods_category` (`goods_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='상품검수 (1차/2차/재고 통합 관리)';


-- fantry_dev.report definition

CREATE TABLE `report` (
                          `report_id` int NOT NULL AUTO_INCREMENT,
                          `report_reason` text,
                          `report_at` datetime DEFAULT CURRENT_TIMESTAMP,
                          `report_status` enum('RESOLVED','RECEIVED','WITHDRAWN','REJECTED') DEFAULT NULL COMMENT '신고 상태: RECEIVED=철회 접수, RESOLVED=완료, WITHDRAWN=사용자 철회, REJECTED=관리자 거절',
                          `rejection_comment` text,
                          `member_id` int NOT NULL,
                          PRIMARY KEY (`report_id`),
                          KEY `fk_report_member` (`member_id`),
                          CONSTRAINT `fk_report_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.`returns` definition

CREATE TABLE `returns` (
                           `return_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                           `order_id` int NOT NULL COMMENT '주문 ID',
                           `return_amount` decimal(15,2) NOT NULL COMMENT '반품 금액',
                           `return_reason` text COMMENT '반품 사유',
                           `status` enum('REQUESTED','APPROVED','REJECTED','COMPLETED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'REQUESTED' COMMENT '반품 상태 (요청됨, 승인됨, 거절됨, 완료됨)',
                           `reject_reason` text COMMENT '반품 거절 사유',
                           `requested_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '반품 요청 시간',
                           `completed_at` datetime DEFAULT NULL COMMENT '반품 완료 시간',
                           `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
                           `created_by` int DEFAULT NULL COMMENT '생성자',
                           `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
                           `updated_by` int DEFAULT NULL COMMENT '처리자',
                           `comment` varchar(255) DEFAULT NULL COMMENT '메모',
                           `member_id` int DEFAULT NULL COMMENT '구매자 ID',
                           PRIMARY KEY (`return_id`),
                           KEY `fk_returns_orders` (`order_id`),
                           KEY `fk_returns_updated_member` (`updated_by`),
                           KEY `fk_returns_created_member` (`created_by`),
                           KEY `fk_returns_member` (`member_id`),
                           CONSTRAINT `fk_returns_created_member` FOREIGN KEY (`created_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                           CONSTRAINT `fk_returns_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                           CONSTRAINT `fk_returns_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`orders_id`),
                           CONSTRAINT `fk_returns_updated_member` FOREIGN KEY (`updated_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.settlement_setting definition

CREATE TABLE `settlement_setting` (
                                      `settlement_setting_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                      `commission_rate` decimal(5,2) NOT NULL COMMENT '수수료율',
                                      `settlement_cycle_days` int DEFAULT NULL COMMENT '정산 주기 일',
                                      `settlement_schedule` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '정산 스케줄 (예: WEEKLY)',
                                      `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
                                      `created_by` int DEFAULT NULL COMMENT '생성자',
                                      `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
                                      `updated_by` int DEFAULT NULL COMMENT '수정자',
                                      PRIMARY KEY (`settlement_setting_id`),
                                      KEY `fk_settlement_setting_created_member` (`created_by`),
                                      KEY `fk_settlement_setting_updated_member` (`updated_by`),
                                      CONSTRAINT `fk_settlement_setting_created_member` FOREIGN KEY (`created_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                                      CONSTRAINT `fk_settlement_setting_updated_member` FOREIGN KEY (`updated_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.account definition

CREATE TABLE `account` (
                           `account_id` int NOT NULL AUTO_INCREMENT,
                           `account_number` varchar(20) NOT NULL,
                           `bank_name` varchar(20) NOT NULL,
                           `create_at` datetime DEFAULT CURRENT_TIMESTAMP,
                           `is_active` tinyint(1) DEFAULT '1' COMMENT '계정 활성 여부: 활성=1(true), 비활성=0(false)',
                           `is_refundable` tinyint(1) DEFAULT '0' COMMENT '환불 계좌 등록: 활성=1(true), 비활성=0(false)',
                           `member_id` int DEFAULT NULL,
                           PRIMARY KEY (`account_id`),
                           KEY `fk_account_member` (`member_id`),
                           CONSTRAINT `fk_account_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.address definition

CREATE TABLE `address` (
                           `address_id` int NOT NULL AUTO_INCREMENT,
                           `destination_address` varchar(300) DEFAULT NULL,
                           `alias` varchar(150) DEFAULT NULL,
                           `recipient_name` varchar(30) DEFAULT NULL,
                           `recipient_tel` varchar(20) DEFAULT NULL,
                           `is_default` tinyint(1) DEFAULT '0' COMMENT '기본 배송지 여부: 기본배송지=1(true), 일반배송지=0(false)',
                           `member_id` int DEFAULT NULL,
                           PRIMARY KEY (`address_id`),
                           KEY `fk_address_member` (`member_id`),
                           CONSTRAINT `fk_address_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.faq definition

CREATE TABLE `faq` (
                       `faq_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                       `cs_type_id` int NOT NULL COMMENT 'FK 문의유형',
                       `question` varchar(50) NOT NULL COMMENT '질문',
                       `answer` mediumtext NOT NULL COMMENT '답변',
                       `status` enum('DRAFT','ACTIVE','PINNED','INACTIVE') NOT NULL COMMENT '임시저장, 활성화(노출), 상단노출, 비활성화(노출안함)',
                       `created_by` int DEFAULT NULL COMMENT '작성자',
                       `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '작성시간',
                       `updated_by` int DEFAULT NULL COMMENT '수정지',
                       `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정시간',
                       PRIMARY KEY (`faq_id`),
                       KEY `fk_faq_cs_type` (`cs_type_id`),
                       KEY `fk_faq_create_member` (`created_by`),
                       KEY `fk_faq_update_member` (`updated_by`),
                       CONSTRAINT `fk_faq_create_member` FOREIGN KEY (`created_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                       CONSTRAINT `fk_faq_cs_type` FOREIGN KEY (`cs_type_id`) REFERENCES `cs_type` (`cs_type_id`),
                       CONSTRAINT `fk_faq_update_member` FOREIGN KEY (`updated_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.filemeta definition

CREATE TABLE `filemeta` (
                            `filemeta_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                            `original_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '원본 파일이름',
                            `stored_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '저장된 파일이름',
                            `stored_file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '저장된 경로',
                            `file_size` int NOT NULL COMMENT '파일 크기',
                            `file_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '파일 유형',
                            `file_ext` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '파일 확장자',
                            `uploaded_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '업로드 시간',
                            `uploaded_by` int DEFAULT NULL COMMENT 'FK 업로드한 유저',
                            `deleted_at` datetime DEFAULT NULL COMMENT '삭제 시간',
                            `width` int DEFAULT NULL COMMENT '가로픽셀(이미지)',
                            `height` int DEFAULT NULL COMMENT '세로픽셀(이미지)',
                            PRIMARY KEY (`filemeta_id`),
                            UNIQUE KEY `stored_file_name` (`stored_file_name`),
                            KEY `fk_filemeta_member` (`uploaded_by`),
                            CONSTRAINT `fk_filemeta_member` FOREIGN KEY (`uploaded_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fantry_dev.product_checklist_answer definition

CREATE TABLE `product_checklist_answer` (
                                            `product_inspection_id` int NOT NULL,
                                            `checklist_role` enum('SELLER','INSPECTOR') NOT NULL,
                                            `item_key` varchar(128) NOT NULL,
                                            `item_label` varchar(256) NOT NULL,
                                            `answer_value` json NOT NULL,
                                            `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
                                            PRIMARY KEY (`product_inspection_id`,`checklist_role`,`item_key`),
                                            CONSTRAINT `fk_pca_product` FOREIGN KEY (`product_inspection_id`) REFERENCES `product_inspection` (`product_inspection_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.settlement definition

CREATE TABLE `settlement` (
                              `settlement_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                              `member_id` int DEFAULT NULL COMMENT '회원 ID',
                              `total_amount` decimal(15,2) NOT NULL COMMENT '총 정산 금액',
                              `commission_amount` decimal(15,2) DEFAULT NULL COMMENT '수수료 금액',
                              `settlement_amount` decimal(15,2) NOT NULL COMMENT '실제 정산 금액',
                              `status` enum('PENDING','PAID','CANCELLED','FAILED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING' COMMENT '정산 상태 (대기, 완료, 취소, 실패)',
                              `failure_reason` text COMMENT '실패 사유',
                              `requested_at` datetime NOT NULL COMMENT '정산 요청 시간',
                              `completed_at` datetime DEFAULT NULL COMMENT '정산 완료 시간',
                              `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
                              `created_by` int DEFAULT NULL COMMENT '생성자',
                              `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
                              `updated_by` int DEFAULT NULL COMMENT '수정자',
                              `total_return_amount` decimal(15,2) DEFAULT NULL COMMENT '총 반품 금액',
                              `account_id` int DEFAULT NULL COMMENT '정산 받을 계좌 ID',
                              PRIMARY KEY (`settlement_id`),
                              KEY `fk_settlement_created_member` (`created_by`),
                              KEY `fk_settlement_updated_member` (`updated_by`),
                              KEY `fk_settlement_member` (`member_id`),
                              KEY `fk_settlement_account` (`account_id`),
                              CONSTRAINT `fk_settlement_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE SET NULL,
                              CONSTRAINT `fk_settlement_created_member` FOREIGN KEY (`created_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                              CONSTRAINT `fk_settlement_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                              CONSTRAINT `fk_settlement_updated_member` FOREIGN KEY (`updated_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- fantry_dev.settlement_item definition

CREATE TABLE `settlement_item` (
                                   `settlement_item_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                   `settlement_id` int NOT NULL COMMENT '정산 ID',
                                   `order_id` int NOT NULL COMMENT '주문 ID',
                                   `return_id` int DEFAULT NULL COMMENT '반품 ID',
                                   `item_sale_amount` decimal(15,2) NOT NULL COMMENT '상품 판매 금액',
                                   `commission_rate` decimal(5,2) DEFAULT NULL COMMENT '수수료율',
                                   `commission_amount` decimal(15,2) DEFAULT NULL COMMENT '수수료 금액',
                                   `total_amount` decimal(15,2) NOT NULL COMMENT '총 정산 금액',
                                   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시간',
                                   `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시간',
                                   `is_returned` tinyint(1) NOT NULL DEFAULT '0' COMMENT '반품 여부',
                                   PRIMARY KEY (`settlement_item_id`),
                                   KEY `fk_settlement_item_settlement` (`settlement_id`),
                                   KEY `fk_settlement_item_returns` (`return_id`),
                                   KEY `fk_settlement_orders` (`order_id`),
                                   CONSTRAINT `fk_settlement_item_returns` FOREIGN KEY (`return_id`) REFERENCES `returns` (`return_id`),
                                   CONSTRAINT `fk_settlement_item_settlement` FOREIGN KEY (`settlement_id`) REFERENCES `settlement` (`settlement_id`),
                                   CONSTRAINT `fk_settlement_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`orders_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fantry_dev.inquiry definition (MODIFIED)

CREATE TABLE `inquiry` (
                           `inquiry_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                           `cs_type_id` int NOT NULL COMMENT 'FK 문의유형',
                           `created_by` int DEFAULT NULL COMMENT 'FK 문의자(멤버ID)',
                           `title` varchar(50) NOT NULL COMMENT '문의 제목',
                           `content` text NOT NULL COMMENT '문의 본문',
                           `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '문의글 작성 시간',
                           `status` enum('PENDING','IN_PROGRESS','ON_HOLD','REJECTED','ANSWERED') NOT NULL DEFAULT 'PENDING' COMMENT '답변대기, 처리중, 보류, 거절(스팸), 답변완료',
                           `answer_content` text COMMENT '답변 내용',
                           `answered_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '답변 시간',
                           `answered_by` int DEFAULT NULL COMMENT 'FK 답변자(멤버ID)',
                           `comment` varchar(255) DEFAULT NULL COMMENT '답변자 메모(특이사항)',
                           PRIMARY KEY (`inquiry_id`),
                           KEY `fk_inquiry_create_member` (`created_by`),
                           KEY `fk_inquiry_cs_type` (`cs_type_id`),
                           KEY `fk_inquiry_answered_member` (`answered_by`),
                           CONSTRAINT `fk_inquiry_answered_member` FOREIGN KEY (`answered_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                           CONSTRAINT `fk_inquiry_create_member` FOREIGN KEY (`created_by`) REFERENCES `member` (`member_id`) ON DELETE SET NULL,
                           CONSTRAINT `fk_inquiry_cs_type` FOREIGN KEY (`cs_type_id`) REFERENCES `cs_type` (`cs_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- fantry_dev.cs_attachment definition

CREATE TABLE `cs_attachment` (
                                 `cs_attachment_id` int NOT NULL AUTO_INCREMENT COMMENT 'PK',
                                 `inquiry_id` int DEFAULT NULL,
                                 `filemeta_id` int DEFAULT NULL,
                                 PRIMARY KEY (`cs_attachment_id`),
                                 KEY `fk_cs_attachement_inquiry` (`inquiry_id`),
                                 KEY `fk_cs_attachement_filemeta` (`filemeta_id`),
                                 CONSTRAINT `fk_cs_attachement_filemeta` FOREIGN KEY (`filemeta_id`) REFERENCES `filemeta` (`filemeta_id`),
                                 CONSTRAINT `fk_cs_attachement_inquiry` FOREIGN KEY (`inquiry_id`) REFERENCES `inquiry` (`inquiry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;