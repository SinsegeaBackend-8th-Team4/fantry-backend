-- Seed Migration (카탈로그 + 체크리스트 + 기준가 + 감가룰)
START TRANSACTION;
-- =========================================================
-- 1) 카탈로그 (카테고리 / 아티스트 / 앨범)
-- =========================================================

-- 1-1) goods_category
INSERT INTO goods_category (code, name)
SELECT code, name FROM (
   SELECT 'PC', '포토카드' UNION ALL
   SELECT 'ALBUM', '앨범' UNION ALL
   SELECT 'DVD', 'DVD/Blu-ray' UNION ALL
   SELECT 'SEASON_GREETING', '시즌그리팅' UNION ALL
   SELECT 'LIGHTSTICK', '응원봉' UNION ALL
   SELECT 'ETC', '기타'
) AS tmp(code, name)
WHERE NOT EXISTS (SELECT 1 FROM goods_category WHERE goods_category.code = tmp.code);

-- 1-2) artist
INSERT INTO artist (name_ko, name_en, group_type)
SELECT name_ko, name_en, group_type
FROM (
 SELECT '방탄소년단', 'BTS', 'MALE_GROUP' UNION ALL
 SELECT '세븐틴', 'SEVENTEEN', 'MALE_GROUP' UNION ALL
 SELECT '엑소', 'EXO', 'MALE_GROUP' UNION ALL
 SELECT '엔시티 127', 'NCT 127', 'MALE_GROUP' UNION ALL
 SELECT '스트레이 키즈', 'Stray Kids', 'MALE_GROUP' UNION ALL
 SELECT '블랙핑크', 'BLACKPINK', 'FEMALE_GROUP' UNION ALL
 SELECT '트와이스', 'TWICE', 'FEMALE_GROUP' UNION ALL
 SELECT '뉴진스', 'NewJeans', 'FEMALE_GROUP' UNION ALL
 SELECT '에스파', 'aespa', 'FEMALE_GROUP' UNION ALL
 SELECT '르세라핌', 'LE SSERAFIM', 'FEMALE_GROUP' UNION ALL
 SELECT '아이유', 'IU', 'FEMALE_SOLO' UNION ALL
 SELECT '태연', 'Taeyeon', 'FEMALE_SOLO' UNION ALL
 SELECT '백현', 'Baekhyun', 'MALE_SOLO'
) AS tmp(name_ko, name_en, group_type)
WHERE NOT EXISTS (SELECT 1 FROM artist WHERE artist.name_en = tmp.name_en);

-- 1-3) album  (artist_id + title(+version) 기준 중복 방지)
-- BTS
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'MAP OF THE SOUL : 7', '2020-02-21', 'Ver.1'
FROM artist WHERE name_en='BTS'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='BTS' AND al.title='MAP OF THE SOUL : 7' AND al.version='Ver.1'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'BE', '2020-11-20', 'Deluxe Edition'
FROM artist WHERE name_en='BTS'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='BTS' AND al.title='BE' AND al.version='Deluxe Edition'
);
-- SEVENTEEN
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'Face the Sun', '2022-05-27', 'Carat Ver.'
FROM artist WHERE name_en='SEVENTEEN'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='SEVENTEEN' AND al.title='Face the Sun' AND al.version='Carat Ver.'
);

-- EXO
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'DON’T MESS UP MY TEMPO', '2018-11-02', 'Ver. Allegro'
FROM artist WHERE name_en='EXO'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='EXO' AND al.title='DON’T MESS UP MY TEMPO' AND al.version='Ver. Allegro'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'EXIST', '2023-07-10', 'Photobook Ver.'
FROM artist WHERE name_en='EXO'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='EXO' AND al.title='EXIST' AND al.version='Photobook Ver.'
);

-- NCT 127
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'Sticker', '2021-09-17', 'Sticky Ver.'
FROM artist WHERE name_en='NCT 127'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='NCT 127' AND al.title='Sticker' AND al.version='Sticky Ver.'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, '2 Baddies', '2022-09-16', 'Ver.1'
FROM artist WHERE name_en='NCT 127'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='NCT 127' AND al.title='2 Baddies' AND al.version='Ver.1'
);

-- Stray Kids
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'NOEASY', '2021-08-23', 'Standard Ver.'
FROM artist WHERE name_en='Stray Kids'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='Stray Kids' AND al.title='NOEASY' AND al.version='Standard Ver.'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, '★★★★★ (5-STAR)', '2023-06-02', 'Standard Ver.'
FROM artist WHERE name_en='Stray Kids'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='Stray Kids' AND al.title='★★★★★ (5-STAR)' AND al.version='Standard Ver.'
);

-- BLACKPINK
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'THE ALBUM', '2020-10-02', 'Ver.1'
FROM artist WHERE name_en='BLACKPINK'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='BLACKPINK' AND al.title='THE ALBUM' AND al.version='Ver.1'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'BORN PINK', '2022-09-16', 'Standard Ver.'
FROM artist WHERE name_en='BLACKPINK'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='BLACKPINK' AND al.title='BORN PINK' AND al.version='Standard Ver.'
);

-- TWICE
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'READY TO BE', '2023-03-10', 'Standard Ver.'
FROM artist WHERE name_en='TWICE'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='TWICE' AND al.title='READY TO BE' AND al.version='Standard Ver.'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'Formula of Love: O+T=<3', '2021-11-12', 'Standard Ver.'
FROM artist WHERE name_en='TWICE'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='TWICE' AND al.title='Formula of Love: O+T=<3' AND al.version='Standard Ver.'
);

-- NewJeans
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'New Jeans 1st EP', '2022-08-01', 'Weverse Ver.'
FROM artist WHERE name_en='NewJeans'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='NewJeans' AND al.title='New Jeans 1st EP' AND al.version='Weverse Ver.'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'Get Up', '2023-07-21', 'Bunny Ver.'
FROM artist WHERE name_en='NewJeans'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='NewJeans' AND al.title='Get Up' AND al.version='Bunny Ver.'
);

-- aespa
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'Savage - The 1st Mini Album', '2021-10-05', 'Standard Ver.'
FROM artist WHERE name_en='aespa'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='aespa' AND al.title='Savage - The 1st Mini Album' AND al.version='Standard Ver.'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'MY WORLD - The 3rd Mini Album', '2023-05-08', 'Standard Ver.'
FROM artist WHERE name_en='aespa'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='aespa' AND al.title='MY WORLD - The 3rd Mini Album' AND al.version='Standard Ver.'
);

-- LE SSERAFIM
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'UNFORGIVEN', '2023-05-01', 'Standard Ver.'
FROM artist WHERE name_en='LE SSERAFIM'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='LE SSERAFIM' AND al.title='UNFORGIVEN' AND al.version='Standard Ver.'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'ANTIFRAGILE', '2022-10-17', 'Standard Ver.'
FROM artist WHERE name_en='LE SSERAFIM'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='LE SSERAFIM' AND al.title='ANTIFRAGILE' AND al.version='Standard Ver.'
);

-- IU / Taeyeon / Baekhyun
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'LILAC', '2021-03-25', NULL
FROM artist WHERE name_en='IU'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='IU' AND al.title='LILAC' AND al.version IS NULL
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'INVU - The 3rd Album', '2022-02-14', 'Standard Ver.'
FROM artist WHERE name_en='Taeyeon'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='Taeyeon' AND al.title='INVU - The 3rd Album' AND al.version='Standard Ver.'
);
INSERT INTO album (artist_id, title, release_date, version)
SELECT artist_id, 'Delight - The 2nd Mini Album', '2020-05-25', 'C Ver.'
FROM artist WHERE name_en='Baekhyun'
AND NOT EXISTS (
    SELECT 1 FROM album al JOIN artist a2 ON al.artist_id=a2.artist_id
    WHERE a2.name_en='Baekhyun' AND al.title='Delight - The 2nd Mini Album' AND al.version='C Ver.'
);

-- =========================================================
-- 2) 체크리스트 템플릿 / 항목 / 카테고리 매핑
-- =========================================================

-- 2-1) checklist_template
INSERT INTO checklist_template (code, role, version, title, status, published_at)
SELECT 'SELLER_V1', 'SELLER', 1, '판매자 1차 온라인 검수', 'PUBLISHED', NOW()
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM checklist_template WHERE code='SELLER_V1' AND role='SELLER' AND version=1
);

-- 템플릿 ID 변수
SET @SELLER_TMPL_ID := (
  SELECT checklist_template_id FROM checklist_template
  WHERE code='SELLER_V1' AND role='SELLER' AND version=1
);

-- 2-2) 공통 항목 정의
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'authenticity_sticker', '정품 인증 스티커 부착', 'BOOL', JSON_ARRAY('Y','N'), 1, 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='authenticity_sticker' AND checklist_template_id=@SELLER_TMPL_ID);

INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'box_condition', '박스/포장 불량 상태', 'SELECT', JSON_ARRAY('없음','일부','다수'), 1, 2
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='box_condition' AND checklist_template_id=@SELLER_TMPL_ID);

INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'components_missing', '구성품 누락', 'SELECT', JSON_ARRAY('없음','일부','다수'), 1, 3
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='components_missing' AND checklist_template_id=@SELLER_TMPL_ID);

-- 상태 설명
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'condition_note', '상태 설명', 'TEXT', NULL, 0, 1000
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='condition_note' AND checklist_template_id=@SELLER_TMPL_ID);

-- 2-3) 카테고리별 전용 항목
-- PC
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'edge_bent', '모서리 꺾임/휨', 'SELECT', JSON_ARRAY('없음','일부','다수'), 1, 11
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='edge_bent' AND checklist_template_id=@SELLER_TMPL_ID);
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'surface_scratch', '표면 스크래치', 'SELECT', JSON_ARRAY('없음','일부','다수'), 1, 12
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='surface_scratch' AND checklist_template_id=@SELLER_TMPL_ID);

-- ALBUM
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'album_version', '앨범 버전 선택', 'SELECT', JSON_ARRAY('일반','한정판'), 0, 51
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='album_version' AND checklist_template_id=@SELLER_TMPL_ID);

-- LIGHTSTICK
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'power_on', '전원 정상작동', 'BOOL', JSON_ARRAY('Y','N'), 1, 21
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='power_on' AND checklist_template_id=@SELLER_TMPL_ID);
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'bluetooth_connect', '블루투스 연결 정상', 'BOOL', JSON_ARRAY('Y','N'), 1, 22
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='bluetooth_connect' AND checklist_template_id=@SELLER_TMPL_ID);

-- SEASON_GREETING
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'seal_status', '개봉 여부', 'BOOL', JSON_ARRAY('Y','N'), 1, 31
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='seal_status' AND checklist_template_id=@SELLER_TMPL_ID);

-- DVD
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'disk_scratch', '디스크 표면 스크래치', 'SELECT', JSON_ARRAY('없음','일부','다수'), 1, 41
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='disk_scratch' AND checklist_template_id=@SELLER_TMPL_ID);
INSERT INTO checklist_item (checklist_template_id, item_key, label, type, options, required, order_index)
SELECT @SELLER_TMPL_ID, 'disk_playable', '디스크 재생 정상 여부', 'BOOL', JSON_ARRAY('Y','N'), 1, 42
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM checklist_item WHERE item_key='disk_playable' AND checklist_template_id=@SELLER_TMPL_ID);

-- 2-4) 카테고리 매핑
-- PC
INSERT INTO checklist_item_category_map (checklist_item_id, goods_category_id)
SELECT ci.checklist_item_id, gc.goods_category_id
FROM checklist_item ci
JOIN goods_category gc ON gc.code='PC'
WHERE ci.checklist_template_id=@SELLER_TMPL_ID
AND ci.item_key IN ('box_condition','components_missing','condition_note','edge_bent','surface_scratch')
AND NOT EXISTS (
    SELECT 1 FROM checklist_item_category_map m
    WHERE m.checklist_item_id=ci.checklist_item_id AND m.goods_category_id=gc.goods_category_id
);

-- ALBUM
INSERT INTO checklist_item_category_map (checklist_item_id, goods_category_id)
SELECT ci.checklist_item_id, gc.goods_category_id
FROM checklist_item ci
JOIN goods_category gc ON gc.code='ALBUM'
WHERE ci.checklist_template_id=@SELLER_TMPL_ID
AND ci.item_key IN ('authenticity_sticker','box_condition','components_missing','condition_note','seal_status','album_version')
AND NOT EXISTS (
    SELECT 1 FROM checklist_item_category_map m
    WHERE m.checklist_item_id=ci.checklist_item_id AND m.goods_category_id=gc.goods_category_id
);

-- LIGHTSTICK
INSERT INTO checklist_item_category_map (checklist_item_id, goods_category_id)
SELECT ci.checklist_item_id, gc.goods_category_id
FROM checklist_item ci
JOIN goods_category gc ON gc.code='LIGHTSTICK'
WHERE ci.checklist_template_id=@SELLER_TMPL_ID
AND ci.item_key IN ('authenticity_sticker','box_condition','components_missing','condition_note','power_on','bluetooth_connect')
AND NOT EXISTS (
    SELECT 1 FROM checklist_item_category_map m
    WHERE m.checklist_item_id=ci.checklist_item_id AND m.goods_category_id=gc.goods_category_id
);

-- SEASON_GREETING
INSERT INTO checklist_item_category_map (checklist_item_id, goods_category_id)
SELECT ci.checklist_item_id, gc.goods_category_id
FROM checklist_item ci
JOIN goods_category gc ON gc.code='SEASON_GREETING'
WHERE ci.checklist_template_id=@SELLER_TMPL_ID
AND ci.item_key IN ('authenticity_sticker','box_condition','components_missing','condition_note','seal_status')
AND NOT EXISTS (
    SELECT 1 FROM checklist_item_category_map m
    WHERE m.checklist_item_id=ci.checklist_item_id AND m.goods_category_id=gc.goods_category_id
);

-- DVD
INSERT INTO checklist_item_category_map (checklist_item_id, goods_category_id)
SELECT ci.checklist_item_id, gc.goods_category_id
FROM checklist_item ci
JOIN goods_category gc ON gc.code='DVD'
WHERE ci.checklist_template_id=@SELLER_TMPL_ID
AND ci.item_key IN ('authenticity_sticker','box_condition','components_missing','condition_note','disk_scratch','disk_playable')
AND NOT EXISTS (
    SELECT 1 FROM checklist_item_category_map m
    WHERE m.checklist_item_id=ci.checklist_item_id AND m.goods_category_id=gc.goods_category_id
);

-- ETC (공통 최소항목만)
INSERT INTO checklist_item_category_map (checklist_item_id, goods_category_id)
SELECT ci.checklist_item_id, gc.goods_category_id
FROM checklist_item ci
JOIN goods_category gc ON gc.code='ETC'
WHERE ci.checklist_template_id=@SELLER_TMPL_ID
AND ci.item_key IN ('authenticity_sticker','box_condition','components_missing','condition_note')
AND NOT EXISTS (
    SELECT 1 FROM checklist_item_category_map m
    WHERE m.checklist_item_id=ci.checklist_item_id AND m.goods_category_id=gc.goods_category_id
);

-- =========================================================
-- 3) PRICE BASELINE (카테고리별 기준가)  단위: 원
-- =========================================================
INSERT INTO price_baseline (goods_category_id, source, amount, effective_at)
SELECT gc.goods_category_id, 'SEED', 20000, NOW()
FROM goods_category gc WHERE gc.code='PC'
AND NOT EXISTS (SELECT 1 FROM price_baseline WHERE goods_category_id=gc.goods_category_id AND source='SEED');

INSERT INTO price_baseline (goods_category_id, source, amount, effective_at)
SELECT gc.goods_category_id, 'SEED', 25000, NOW()
FROM goods_category gc WHERE gc.code='ALBUM'
AND NOT EXISTS (SELECT 1 FROM price_baseline WHERE goods_category_id=gc.goods_category_id AND source='SEED');

INSERT INTO price_baseline (goods_category_id, source, amount, effective_at)
SELECT gc.goods_category_id, 'SEED', 60000, NOW()
FROM goods_category gc WHERE gc.code='DVD'
AND NOT EXISTS (SELECT 1 FROM price_baseline WHERE goods_category_id=gc.goods_category_id AND source='SEED');

INSERT INTO price_baseline (goods_category_id, source, amount, effective_at)
SELECT gc.goods_category_id, 'SEED', 50000, NOW()
FROM goods_category gc WHERE gc.code='SEASON_GREETING'
AND NOT EXISTS (SELECT 1 FROM price_baseline WHERE goods_category_id=gc.goods_category_id AND source='SEED');

INSERT INTO price_baseline (goods_category_id, source, amount, effective_at)
SELECT gc.goods_category_id, 'SEED', 45000, NOW()
FROM goods_category gc WHERE gc.code='LIGHTSTICK'
AND NOT EXISTS (SELECT 1 FROM price_baseline WHERE goods_category_id=gc.goods_category_id AND source='SEED');

INSERT INTO price_baseline (goods_category_id, source, amount, effective_at)
SELECT gc.goods_category_id, 'SEED', 15000, NOW()
FROM goods_category gc WHERE gc.code='ETC'
AND NOT EXISTS (SELECT 1 FROM price_baseline WHERE goods_category_id=gc.goods_category_id AND source='SEED');

-- =========================================================
-- 4) PRICING RULES (감가 정책)
--  - goods_category_id IS NULL = 공통룰
--  - effective_type: PCT(%) / ABS(원) / CAP(하한배수)
-- =========================================================

-- 4-1) 공통(NULL)
-- 박스 상태 (완화): 일부 -5%, 다수 -15%
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT NULL, 'box_condition', '일부', 'PCT', -0.05, 1, NOW()
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM pricing_rule WHERE goods_category_id IS NULL AND item_key='box_condition' AND option_value='일부'
);
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT NULL, 'box_condition', '다수', 'PCT', -0.15, 1, NOW()
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM pricing_rule WHERE goods_category_id IS NULL AND item_key='box_condition' AND option_value='다수'
);

-- 구성품 누락: 일부 -10%, 다수 -30%
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT NULL, 'components_missing', '일부', 'PCT', -0.10, 1, NOW()
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM pricing_rule WHERE goods_category_id IS NULL AND item_key='components_missing' AND option_value='일부'
);
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT NULL, 'components_missing', '다수', 'PCT', -0.30, 1, NOW()
FROM DUAL WHERE NOT EXISTS (
    SELECT 1 FROM pricing_rule WHERE goods_category_id IS NULL AND item_key='components_missing' AND option_value='다수'
);

-- 4-2) PC (포토카드)
-- edge_bent / surface_scratch : 일부 -10%, 다수 -30%
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'edge_bent', '일부', 'PCT', -0.10, 1, NOW()
FROM goods_category gc WHERE gc.code='PC'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='edge_bent' AND option_value='일부');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'edge_bent', '다수', 'PCT', -0.30, 1, NOW()
FROM goods_category gc WHERE gc.code='PC'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='edge_bent' AND option_value='다수');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'surface_scratch', '일부', 'PCT', -0.10, 1, NOW()
FROM goods_category gc WHERE gc.code='PC'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='surface_scratch' AND option_value='일부');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'surface_scratch', '다수', 'PCT', -0.30, 1, NOW()
FROM goods_category gc WHERE gc.code='PC'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='surface_scratch' AND option_value='다수');

-- 4-3) ALBUM
-- authenticity_sticker: N → -5%, seal_status: Y → -20%, album_version: 한정판 +30%
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'authenticity_sticker', 'N', 'PCT', -0.05, 1, NOW()
FROM goods_category gc WHERE gc.code='ALBUM'
                         AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='authenticity_sticker' AND option_value='N');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'seal_status', 'Y', 'PCT', -0.20, 1, NOW()
FROM goods_category gc WHERE gc.code='ALBUM'
                         AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='seal_status' AND option_value='Y');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'album_version', '한정판', 'PCT', 0.30, 1, NOW()
FROM goods_category gc WHERE gc.code='ALBUM'
                         AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='album_version' AND option_value='한정판');

-- 4-4) LIGHTSTICK
-- authenticity_sticker: N → -5%, power_on: N → -30%, bluetooth_connect: N → -10%
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'authenticity_sticker', 'N', 'PCT', -0.05, 1, NOW()
FROM goods_category gc WHERE gc.code='LIGHTSTICK'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='authenticity_sticker' AND option_value='N');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'power_on', 'N', 'PCT', -0.30, 1, NOW()
FROM goods_category gc WHERE gc.code='LIGHTSTICK'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='power_on' AND option_value='N');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'bluetooth_connect', 'N', 'PCT', -0.10, 1, NOW()
FROM goods_category gc WHERE gc.code='LIGHTSTICK'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='bluetooth_connect' AND option_value='N');

-- 4-5) SEASON_GREETING
-- authenticity_sticker: N → -5%, seal_status: Y(개봉) → -20%
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'authenticity_sticker', 'N', 'PCT', -0.05, 1, NOW()
FROM goods_category gc WHERE gc.code='SEASON_GREETING'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='authenticity_sticker' AND option_value='N');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'seal_status', 'Y', 'PCT', -0.20, 1, NOW()
FROM goods_category gc WHERE gc.code='SEASON_GREETING'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='seal_status' AND option_value='Y');

-- 4-6) DVD
-- authenticity_sticker: N → -5%, disk_scratch 일부/다수 -10/-30, disk_playable N -50
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'authenticity_sticker', 'N', 'PCT', -0.05, 1, NOW()
FROM goods_category gc WHERE gc.code='DVD'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='authenticity_sticker' AND option_value='N');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'disk_scratch', '일부', 'PCT', -0.10, 1, NOW()
FROM goods_category gc WHERE gc.code='DVD'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='disk_scratch' AND option_value='일부');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'disk_scratch', '다수', 'PCT', -0.30, 1, NOW()
FROM goods_category gc WHERE gc.code='DVD'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='disk_scratch' AND option_value='다수');
INSERT INTO pricing_rule (goods_category_id, item_key, option_value, effective_type, pct_value, active, created_at)
SELECT gc.goods_category_id, 'disk_playable', 'N', 'PCT', -0.50, 1, NOW()
FROM goods_category gc WHERE gc.code='DVD'
AND NOT EXISTS (SELECT 1 FROM pricing_rule WHERE goods_category_id=gc.goods_category_id AND item_key='disk_playable' AND option_value='N');

COMMIT;
