# 환불 및 정산 API 문서

이 문서는 환불/반품 및 정산 관련 API 요청 및 응답 형식을 설명합니다. 클라이언트 개발자가 관련 기능을 쉽게 통합할 수 있도록 돕기 위해 작성되었습니다.

---

## 1. 환불/반품 API

### 1.1. 관리자용 환불/반품 API (`ReturnAdminController`)

**기본 경로:** `/api/admin/returns`

#### 1.1.1. 환불/반품 요청 통계 조회
- **HTTP 메서드:** `GET`
- **경로:** `/api/admin/returns/stats`
- **설명:** 관리자 대시보드에 표시될 환불/반품 요청 통계를 조회합니다.
- **응답 타입:** `ReturnStatsAdminResponse`
- **응답 구조:**
```java
public record ReturnStatsAdminResponse(
        long requested, // 요청됨
        long inTransit, // 수거 중
        long inspecting, // 검수 중
        long approved, // 승인됨
        long rejected, // 거절됨
        long completed, // 처리 완료
        long userCancelled, // 사용자 철회
        long total // 총 요청 건수 (DELETED 제외)
) {}
```

#### 1.1.2. 관리자 환불/반품 요청 생성
- **HTTP 메서드:** `POST`
- **경로:** `/api/admin/returns`
- **설명:** 관리자가 사용자를 대신하여 환불/반품 요청을 생성합니다.
- **요청 본문:** `ReturnAdminCreateRequest`
```java
public record ReturnAdminCreateRequest(
        @NotBlank String orderId, // 주문 ID
        @NotNull int memberId, // 회원 ID
        @NotNull ReturnReason reason, // 환불/반품 사유 (SIMPLE_CHANGE_OF_MIND, PRODUCT_DEFECT, SHIPPING_ERROR, ETC)
        String detailReason // 상세 사유
) {}
```
- **응답 타입:** `ReturnAdminResponse`
- **응답 구조:** (아래 1.1.4. 참조)

#### 1.1.3. 환불/반품 요청 목록 검색
- **HTTP 메서드:** `GET`
- **경로:** `/api/admin/returns`
- **설명:** 검색 조건에 따라 페이징된 환불/반품 요청 목록을 조회합니다.
- **요청 파라미터:** `ReturnSearchRequest` (ModelAttribute), `Pageable`
```java
public record ReturnSearchRequest(
        ReturnStatus status, // 검색할 상태 (REQUESTED, IN_TRANSIT, INSPECTING, APPROVED, REJECTED, COMPLETED, USER_CANCELLED, DELETED)
        String buyerName // 구매자 이름
) {}
```
- **응답 타입:** `Page<ReturnSummaryResponse>`
- **`ReturnSummaryResponse` 구조:**
```java
public record ReturnSummaryResponse(
        int returnRequestId, // 환불/반품 요청 ID
        int orderId, // 주문 ID
        String buyerName, // 구매자 이름
        ReturnStatus status, // 현재 상태
        LocalDateTime requestedAt // 요청 일시
) {}
```

#### 1.1.4. 특정 환불/반품 요청 상세 조회
- **HTTP 메서드:** `GET`
- **경로:** `/api/admin/returns/{returnRequestId}`
- **설명:** 특정 환불/반품 요청의 상세 정보를 조회합니다.
- **응답 타입:** `ReturnAdminResponse`
- **응답 구조:**
```java
public record ReturnAdminResponse(
        int returnRequestId,
        int orderId,
        String buyerName,
        String createdBy, // 생성 관리자 이름
        String updatedBy, // 최종 처리 관리자 이름
        ReturnReason reason,
        String detailReason,
        ReturnStatus status,
        BigDecimal originalPaymentAmount, // 원 결제 금액
        BigDecimal deductedShippingFee, // 차감된 배송비
        BigDecimal finalRefundAmount, // 최종 환불 금액
        String rejectReason, // 거절 사유
        String comment, // 관리자 메모
        LocalDateTime requestedAt,
        LocalDateTime completedAt,
        List<String> attachmentUrls, // 첨부파일 URL 목록
        List<ReturnHistoryResponse> statusHistories // 상태 변경 이력
) {}
```
- **`ReturnHistoryResponse` 구조:**
```java
public record ReturnHistoryResponse(
        ReturnStatus previousStatus, // 이전 상태
        ReturnStatus newStatus, // 변경된 상태
        String updatedBy, // 변경자 (관리자 이름 또는 System)
        LocalDateTime updatedAt, // 변경 일시
        String memo // 변경 메모
) {}
```

#### 1.1.5. 환불/반품 요청 상태 업데이트
- **HTTP 메서드:** `PATCH`
- **경로:** `/api/admin/returns/{returnRequestId}`
- **설명:** 특정 환불/반품 요청의 상태를 변경하고 관련 정보를 업데이트합니다.
- **요청 본문:** `ReturnAdminUpdateRequest`
```java
public record ReturnAdminUpdateRequest(
        @NotNull ReturnStatus status, // 변경할 상태 (REQUESTED, IN_TRANSIT, INSPECTING, APPROVED, REJECTED, COMPLETED)
        BigDecimal deductedShippingFee, // 차감할 배송비 (선택 사항)
        String rejectReason, // 거절 사유 (REJECTED 상태 시 필요)
        String memo // 관리자 메모
) {}
```
- **응답 타입:** `ReturnAdminResponse`
- **응답 구조:** (위 1.1.4. 참조)

#### 1.1.6. 환불/반품 요청 삭제 (논리적)
- **HTTP 메서드:** `DELETE`
- **경로:** `/api/admin/returns/{returnRequestId}`
- **설명:** 특정 환불/반품 요청을 논리적으로 삭제(숨김 처리)합니다.
- **응답 타입:** `204 No Content`

### 1.2. 사용자용 환불/반품 API (`ReturnController`)

**기본 경로:** `/api/returns`

#### 1.2.1. 새로운 환불/반품 요청
- **HTTP 메서드:** `POST`
- **경로:** `/api/returns`
- **설명:** 새로운 환불/반품 요청을 생성합니다.
- **요청 본문:** `ReturnCreateRequest`
```java
public record ReturnCreateRequest(
        @NotBlank String orderId, // 주문 ID
        @NotNull ReturnReason reason, // 환불/반품 사유 (SIMPLE_CHANGE_OF_MIND, PRODUCT_DEFECT, SHIPPING_ERROR, ETC)
        @Size(max = 2000) String detailReason // 상세 사유
) {}
```
- **응답 타입:** `ReturnDetailResponse`
- **응답 구조:** (아래 1.2.3. 참조)

#### 1.2.2. 환불/반품 요청에 증빙 자료 첨부
- **HTTP 메서드:** `POST`
- **경로:** `/api/returns/{returnRequestId}/attachments`
- **설명:** 특정 환불/반품 요청에 증빙 자료(파일)를 첨부합니다.
- **요청 파라미터:** `returnRequestId` (경로 변수), `files` (MultipartFile 목록)
- **Content-Type:** `multipart/form-data`
- **응답 타입:** `200 OK`

#### 1.2.3. 나의 환불/반품 요청 목록 조회
- **HTTP 메서드:** `GET`
- **경로:** `/api/returns`
- **설명:** 현재 로그인한 사용자의 환불/반품 요청 목록을 페이징하여 조회합니다.
- **요청 파라미터:** `Pageable`
- **응답 타입:** `Page<ReturnSummaryResponse>`
- **`ReturnSummaryResponse` 구조:** (위 1.1.3. 참조)

#### 1.2.4. 나의 특정 환불/반품 요청 상세 조회
- **HTTP 메서드:** `GET`
- **경로:** `/api/returns/{returnRequestId}`
- **설명:** 현재 로그인한 사용자의 특정 환불/반품 요청 상세 정보를 조회합니다.
- **응답 타입:** `ReturnDetailResponse`
- **응답 구조:**
```java
public record ReturnDetailResponse(
        int returnRequestId,
        int orderId,
        ReturnReason reason,
        String detailReason,
        ReturnStatus status,
        BigDecimal finalRefundAmount, // 최종 환불 금액
        LocalDateTime requestedAt,
        LocalDateTime completedAt,
        List<String> attachmentUrls, // 첨부파일 URL 목록
        List<ReturnHistoryResponse> statusHistories // 상태 변경 이력
) {}
```
- **`ReturnHistoryResponse` 구조:** (위 1.1.4. 참조)

#### 1.2.5. 나의 환불/반품 요청 철회
- **HTTP 메서드:** `PATCH`
- **경로:** `/api/returns/{returnRequestId}/cancel`
- **설명:** 사용자가 직접 자신의 환불/반품 요청을 철회합니다. (처리 중 상태가 되기 전에만 가능)
- **응답 타입:** `204 No Content`

---

## 2. 정산 API

### 2.1. 관리자용 정산 API (`SettlementAdminController`)

**기본 경로:** `/api/admin/settlement`

#### 2.1.1. 정산 대시보드 요약 조회
- **HTTP 메서드:** `GET`
- **경로:** `/api/admin/settlement/dashboard`
- **설명:** 관리자 대시보드에 표시될 정산 KPI 요약 정보를 조회합니다.
- **응답 타입:** `SettlementDashboardResponse`
- **응답 구조:**
```java
public record SettlementDashboardResponse(
        BigDecimal monthlyScheduledAmount, // 당월 정산 예정액
        BigDecimal yesterdaySettledAmount, // 어제 정산된 금액
        long pendingOrFailedCount,     // 정산 보류/실패 건수
        BigDecimal cumulativeSettlementAmount // 누적 정산액
) {}
```

#### 2.1.2. 정산 내역 목록 검색
- **HTTP 메서드:** `GET`
- **경로:** `/api/admin/settlement`
- **설명:** 검색 조건과 함께 페이징된 정산 내역 목록을 조회합니다.
- **요청 파라미터:** `SettlementSearchCondition` (ModelAttribute), `Pageable`
```java
@Getter @Setter
public class SettlementSearchCondition {
    private String sellerName; // 판매자 이름
    private SettlementStatus status; // 정산 상태
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate; // 시작일
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate; // 종료일
}
```
- **응답 타입:** `Page<SettlementSummaryResponse>`
- **`SettlementSummaryResponse` 구조:**
```java
public record SettlementSummaryResponse(
        int settlementId, // 정산 ID
        String sellerName, // 판매자 이름
        BigDecimal settlementAmount, // 정산 금액
        SettlementStatus status, // 정산 상태
        LocalDateTime requestedAt, // 요청 일시
        LocalDateTime completedAt // 완료 일시
) {}
```

#### 2.1.3. 특정 정산 건 상세 조회
- **HTTP 메서드:** `GET`
- **경로:** `/api/admin/settlement/{settlementId}`
- **설명:** 특정 정산 건의 상세 내역을 조회합니다.
- **응답 타입:** `SettlementDetailResponse`
- **응답 구조:**
```java
public record SettlementDetailResponse(
        int settlementId,
        String sellerName,
        BigDecimal totalAmount, // 총 금액
        BigDecimal commissionAmount, // 수수료 금액
        BigDecimal settlementAmount, // 정산 금액
        SettlementStatus status,
        LocalDateTime requestedAt,
        LocalDateTime completedAt,
        String failureReason, // 실패 사유
        String bankName, // 은행명
        String accountNumber, // 계좌번호
        List<SettlementItemDto> items // 정산 항목 목록
) {}
```
- **`SettlementItemDto` 구조:**
```java
public record SettlementItemDto(
        int orderId, // 주문 ID
        String productName, // 상품명
        BigDecimal itemSaleAmount, // 항목 판매 금액
        BigDecimal commissionRate, // 수수료율
        BigDecimal commissionAmount, // 수수료 금액
        BigDecimal totalAmount, // 총 금액
        boolean isReturned // 반품 여부
) {}
```

#### 2.1.4. 정산 설정 조회
- **HTTP 메서드:** `GET`
- **경로:** `/api/admin/settlement/settings`
- **설명:** 현재 적용 중인 정산 설정을 조회합니다.
- **응답 타입:** `SettlementSettingResponse`
- **응답 구조:**
```java
public record SettlementSettingResponse(
        int settlementSettingId,
        BigDecimal commissionRate, // 수수료율
        SettlementCycleType settlementCycleType, // 정산 주기 타입
        Integer settlementDay, // 정산일
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy
) {}
```

#### 2.1.5. 정산 설정 생성 또는 수정
- **HTTP 메서드:** `POST`
- **경로:** `/api/admin/settlement/settings`
- **설명:** 정산 설정을 생성하거나 수정합니다.
- **요청 본문:** `SettlementSettingRequest`
```java
public record SettlementSettingRequest(
        @NotNull BigDecimal commissionRate, // 수수료율
        @NotNull SettlementCycleType settlementCycleType, // 정산 주기 타입
        Integer settlementDay // 정산일
) {}
```
- **응답 타입:** `SettlementSettingResponse`
- **응답 구조:** (위 2.1.4. 참조)

### 2.2. 판매자용 정산 API (`SettlementController`)

**기본 경로:** `/api/my/settlements`

#### 2.2.1. 나의 정산 내역 목록 조회
- **HTTP 메서드:** `GET`
- **경로:** `/api/my/settlements`
- **설명:** 현재 로그인한 판매자의 정산 내역 목록을 페이징하여 조회합니다.
- **요청 파라미터:** `Pageable`
- **응답 타입:** `Page<SettlementSummaryResponse>`
- **`SettlementSummaryResponse` 구조:** (위 2.1.2. 참조)

#### 2.2.2. 나의 특정 정산 건 상세 조회
- **HTTP 메서드:** `GET`
- **경로:** `/api/my/settlements/{settlementId}`
- **설명:** 현재 로그인한 판매자의 특정 정산 건 상세 내역을 조회합니다.
- **응답 타입:** `SettlementDetailResponse`
- **응답 구조:** (위 2.1.3. 참조)
