-- 입찰 로직을 원자적으로 처리하는 Lua 스크립트
--
-- @param KEYS[1] 경매 최고 입찰가 키 (e.g., "auction:highest_bid:{auctionId}")
--
-- @param ARGV[1] 새로 들어온 입찰가 (bidAmount)
-- @param ARGV[2] 경매 시작가 (startPrice)
-- @param ARGV[3] 최소 입찰 증가액 (minIncrement)
--
-- @return string 성공 또는 실패 상태 코드
--         - "ERROR:INVALID_BID_AMOUNT":      입찰가 인자가 유효하지 않음 (nil)
--         - "ERROR:INVALID_START_PRICE":     시작가 인자가 유효하지 않음 (nil)
--         - "ERROR:INVALID_MIN_INCREMENT":   최소 증가액 인자가 유효하지 않음 (nil)
--         - "ERROR:INVALID_UNIT":            입찰 단위가 유효하지 않음 (예: 100원 단위 위반)
--         - "ERROR:NON_NUMERIC_STORED_VALUE": Redis에 저장된 기존 입찰가가 숫자가 아님
--         - "SUCCESS_FIRST_BID":             첫 입찰 성공
--         - "SUCCESS_HIGHER_BID":            기존가보다 높은 입찰 성공
--         - "FAILURE_TOO_LOW_START":         입찰가가 시작가보다 낮거나 같음
--         - "FAILURE_TOO_LOW_INCREMENT":     입찰가가 현재가+최소증가액보다 낮음
---

local highestBidKey = KEYS[1]
local newBidAmount = tonumber(ARGV[1])
local startPrice = tonumber(ARGV[2])
local minIncrement = tonumber(ARGV[3]) -- 최소 입찰 증가액 (프론트 기준 1000)

-- [검증 1] 인자 유효성 검증
if not newBidAmount then return "ERROR:INVALID_BID_AMOUNT" end
if not startPrice then return "ERROR:INVALID_START_PRICE" end
if not minIncrement then return "ERROR:INVALID_MIN_INCREMENT" end

-- [검증 2] 입찰 단위 검증
if newBidAmount % 100 ~= 0 then return "ERROR:INVALID_UNIT" end

-- [검증 3] 저장된 데이터 유효성 검증
local currentStr = redis.call('GET', highestBidKey)
local currentHighestBid = nil
if currentStr then
    currentHighestBid = tonumber(currentStr)
    if not currentHighestBid then
        return "ERROR:NON_NUMERIC_STORED_VALUE"
    end
end

-- [핵심 로직]
if not currentHighestBid then
    -- Case 1: 첫 입찰인 경우 (프론트 기준: 시작가보다 '초과'해야 함)
    if newBidAmount > startPrice then
        redis.call('SET', highestBidKey, newBidAmount)
        return "SUCCESS_FIRST_BID"
    else
        return "FAILURE_TOO_LOW_START"
    end
else
    -- Case 2: 기존 입찰이 있는 경우 (프론트 기준: 현재가 + 1000원 '이상'이어야 함)
    if newBidAmount >= currentHighestBid + minIncrement then
        redis.call('SET', highestBidKey, newBidAmount)
        return "SUCCESS_HIGHER_BID"
    else
        return "FAILURE_TOO_LOW_INCREMENT"
    end
end