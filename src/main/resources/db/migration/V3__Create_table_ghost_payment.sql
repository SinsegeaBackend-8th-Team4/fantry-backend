CREATE TABLE `ghost_payment`
(
    `ghost_payment_id` int                                                          NOT NULL AUTO_INCREMENT,
    `receipt_id`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `status`           varchar(20) DEFAULT NULL,
    PRIMARY KEY (`ghost_payment_id`),
    UNIQUE KEY `receipt_id` (`receipt_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;