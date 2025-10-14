-- auction , orders , bid 테이블 성능 최적화용 인덱스 추가

CREATE INDEX idx_auction_sale_type_status ON auction (sale_type, sale_status);
CREATE INDEX idx_auction_status_endtime ON auction (sale_status, end_time);
CREATE INDEX idx_auction_fk_product_inspection ON auction (product_inspection_id);

CREATE INDEX idx_orders_buyer_status ON orders (buyer_id, order_status);
CREATE INDEX idx_orders_fk_auction ON orders (auction_id);
CREATE INDEX idx_orders_fk_payment ON orders (payment_id);

CREATE INDEX idx_bid_item_at ON bid (item_id, bid_at DESC);
CREATE INDEX idx_bid_bidder ON bid (bidder_id);