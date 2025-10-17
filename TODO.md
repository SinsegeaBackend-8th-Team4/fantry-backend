## 정산(Settlement) 기능 개선을 위한 TODO

이 문서는 현재 정산 기능의 한계점을 명확히 하고, 안정적인 기능 구현을 위해 필요한 개선 작업을 정리합니다.

### 1. `Orders` 엔티티에 판매자(Seller) 정보 직접 연결 (필수)

- **문제점:** 현재 정산 로직은 `Orders` -> `Auction` -> `ProductInspection` -> `Member` 라는 복잡하고 불안정한 경로를 통해 판매자 정보를 조회하고 있습니다. 이 구조는 경매를 통해 판매된 주문에만 한정되어 있어, 향후 고정가 판매 등 다른 방식의 주문이 추가될 경우 정산 대상에서 누락되는 심각한 문제를 야기합니다.
- **해결 방안:** `Orders` 엔티티에 판매자(`seller`) 정보를 직접 저장하도록 스키마와 코드를 수정합니다.
- **기대 효과:**
    - 정산 로직과 특정 판매 방식(경매) 간의 강한 의존성 제거
    - 향후 다양한 판매 방식에 유연하게 대응 가능한 확장성 확보
    - 코드 복잡도 감소 및 가독성 향상

**수정 예시 (`Orders.java`):**
```java
// ... 기존 코드 ...

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "seller_id", referencedColumnName = "member_id")
@Schema(description = "판매자 정보")
private Member seller;

// ... 기존 코드 ...
```

### 2. `OrdersRepository`에 정산 대상 조회 전용 메서드 추가

- **문제점:** 현재 정산 서비스(`SettlementAdminService`)는 `updatedAt`을 기준으로 정산 대상을 조회하는 `findByOrderStatusAndUpdatedAtBefore` 메서드를 사용하고 있습니다. 하지만 이는 정산의 기준이 되어야 할 '배송 완료' 시점과 정확히 일치하지 않을 수 있습니다.
- **해결 방안:** `Orders` 엔티티의 `deliveredAt` 필드를 기준으로 정산 대상을 조회하는 새로운 메서드를 `OrdersRepository`에 추가합니다.
- **기대 효과:**
    - 정산 대상 선정 기준의 정확성 및 신뢰도 향상

**추가 예시 (`OrdersRepository.java`):**
```java
/**
 * 특정 상태와 배송 완료 시간을 기준으로 주문을 조회합니다. (정산 대상 조회용)
 * @param orderStatus 조회할 주문 상태
 * @param cutoffDate 기준 시간 (배송 완료 시간)
 * @return 주문 목록
 */
List<Orders> findByOrderStatusAndDeliveredAtBefore(OrderStatus orderStatus, LocalDateTime cutoffDate);
```

### 3. 환불/반품 정책 연동

- **문제점:** 현재 `SettlementItem`을 생성할 때, 반품 여부를 나타내는 `isReturned` 필드가 `false`로 하드코딩되어 있습니다. 이로 인해 고객이 반품/환불한 주문 건에 대해서도 정산이 이루어질 수 있는 위험이 있습니다.
- **해결 방안:** 주문의 상태(`OrderStatus`) 또는 별도의 환불/반품 도메인 정보를 확인하여, `isReturned` 플래그를 올바르게 설정하는 로직을 구현해야 합니다.
- **기대 효과:**
    - 잘못된 정산 지급 방지 및 재무적 안정성 확보

### 4. `SettlementAdminService` 리팩토링

- **설명:** 위 1, 2번 항목의 수정이 완료된 후, `SettlementAdminService`의 `executeSettlement` 메서드를 다음과 같이 리팩토링합니다.
- **수정 내용:**
    - 판매자 정보를 `order.getSeller()`를 통해 직접 조회하도록 변경합니다.
    - 정산 대상 주문을 `ordersRepository.findByOrderStatusAndDeliveredAtBefore()` 메서드를 통해 조회하도록 변경합니다.
    - 복잡한 필터링 및 그룹화 로직을 단순화합니다.
- **기대 효과:**
    - 코드의 안정성, 가독성, 유지보수성 향상
