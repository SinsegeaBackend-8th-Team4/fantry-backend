-- 1. auction 테이블 변경
ALTER TABLE `auction`
    CHANGE COLUMN `inventory_item_id` `product_inspection_id` INT;

-- auction 테이블에 product_inspection_id 외래 키 추가
ALTER TABLE `auction`
    ADD CONSTRAINT `fk_auction_product_inspection`
        FOREIGN KEY (`product_inspection_id`)
            REFERENCES `product_inspection` (`product_inspection_id`);

-- 2. bid 테이블 변경
-- bidder_id 컬럼 타입을 VARCHAR(20)에서 INT로 변경
ALTER TABLE `bid`
    MODIFY COLUMN `bidder_id` INT;

-- bid 테이블에 bidder_id 외래 키 추가
ALTER TABLE `bid`
    ADD CONSTRAINT `fk_bid_member`
        FOREIGN KEY (`bidder_id`)
            REFERENCES `member` (`member_id`);

-- bid 테이블에 item_id 컬럼 추가
ALTER TABLE `bid`
    ADD COLUMN `item_id` INT;


-- 3. orders 테이블 변경
-- orders 테이블에 auction_id 외래 키 추가
ALTER TABLE `orders`
    ADD CONSTRAINT `fk_orders_auction`
        FOREIGN KEY (`auction_id`)
            REFERENCES `auction` (`auction_id`);

-- orders 테이블에 buyer_id 외래 키 추가
ALTER TABLE `orders`
    ADD CONSTRAINT `fk_orders_buyer`
        FOREIGN KEY (`buyer_id`)
            REFERENCES `member` (`member_id`);

-- orders 테이블에 payment_id 외래 키 추가
ALTER TABLE `orders`
    ADD CONSTRAINT `fk_orders_payment`
        FOREIGN KEY (`payment_id`)
            REFERENCES `payment` (`payment_id`);