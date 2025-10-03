-- 검수 파일 테이블
CREATE TABLE inspection_file (
    inspection_file_id INT NOT NULL AUTO_INCREMENT COMMENT '검수-파일 매핑 ID',
    product_inspection_id INT NOT NULL COMMENT '검수 ID',
    filemeta_id INT NOT NULL COMMENT '파일메타 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    PRIMARY KEY (inspection_file_id),
    KEY fk_if_inspection (product_inspection_id),
    KEY fk_if_filemeta (filemeta_id),
    CONSTRAINT fk_if_inspection FOREIGN KEY (product_inspection_id)
     REFERENCES product_inspection (product_inspection_id),
    CONSTRAINT fk_if_filemeta FOREIGN KEY (filemeta_id)
     REFERENCES filemeta (filemeta_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='검수와 업로드 파일 매핑';

-- 가격 룰 컬럼 속성 변경
ALTER TABLE pricing_rule MODIFY goods_category_id INT NULL;