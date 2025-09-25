ALTER TABLE fantry_dev.filemeta MODIFY COLUMN file_type enum('IMAGE','VIDEO','AUDIO','DOCUMENT','ETC') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '파일 유형';
