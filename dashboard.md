# 대시보드 API 문서

이 문서는 대시보드 관련 API 요청 및 응답 형식을 설명합니다. 클라이언트 개발자가 대시보드 데이터를 쉽게 통합할 수 있도록 돕기 위해 작성되었습니다.

---

## 1. 통합 대시보드 조회

### 요청
- **HTTP 메서드:** `GET`
- **경로:** `/api/dashboard/integrated`
- **설명:** 시스템의 전반적인 현황을 요약한 데이터를 반환합니다.

### 응답
- **타입:** `DashboardStats`
- **구조:**
```java
public class DashboardStats {
    private final long totalUsers; // 총 사용자 수
    private final long todayRegisteredUsers; // 오늘 가입한 사용자 수
    private final long totalAuctions; // 총 경매 수
    private final long ongoingAuctions; // 진행 중인 경매 수
    private final long totalInquiries; // 총 문의 수
    private final long unansweredInquiries; // 미답변 문의 수
    private final long totalPayments; // 총 결제 금액
    private final long todayPayments; // 오늘 결제 금액
    private final long totalSettlements; // 총 정산 건수
    private final long pendingSettlements; // 미정산 건수
    private final long totalRefunds; // 총 환불 건수
    private final long requestedRefunds; // 환불 요청 건수
    private final long totalNotices; // 총 공지사항 수
    private final long activeNotices; // 활성 공지사항 수
    private final long totalFaqs; // 총 FAQ 수
    private final long activeFaqs; // 활성 FAQ 수
    private final long totalBids; // 총 입찰 수
    private final long bidsToday; // 오늘 입찰 수
    private final long totalAccounts; // 총 계정 수
    private final long activeAccounts; // 활성 계정 수
    private final long totalArtists; // 총 아티스트 수
    private final long approvedArtists; // 승인된 아티스트 수
    private final long totalInspections; // 총 검수 건수
    private final long submittedInspections; // 제출된 검수 건수
    private final long totalChecklistTemplates; // 총 체크리스트 템플릿 수
    private final long totalReports; // 총 신고 건수
    private final long receivedReports; // 접수된 신고 건수
}
```

---

## 2. 회원 관리 대시보드 조회

### 요청
- **HTTP 메서드:** `GET`
- **경로:** `/api/dashboard/members`
- **설명:** 회원 관련 통계 데이터를 반환합니다.

### 응답
- **타입:** `MemberDashboardStats`
- **구조:**
```java
public class MemberDashboardStats {
    private final MemberStats memberStats; // 회원 통계
    private final ReportStats reportStats; // 신고 통계
}
```
- **`MemberStats` 구조:**
```java
public class MemberStats {
    private final long totalMembers; // 총 회원 수
    private final long todayRegisteredMembers; // 오늘 가입한 회원 수
}
```
- **`ReportStats` 구조:**
```java
public class ReportStats {
    private final long totalReports; // 총 신고 건수
    private final long resolvedReports; // 해결된 신고 건수
    private final long receivedReports; // 접수된 신고 건수
    private final long withdrawnReports; // 철회된 신고 건수
    private final long rejectedReports; // 거부된 신고 건수
}
```

---

## 3. 주문 관리 대시보드 조회

### 요청
- **HTTP 메서드:** `GET`
- **경로:** `/api/dashboard/orders`
- **설명:** 주문 관련 통계 데이터를 반환합니다.

### 응답
- **타입:** `OrdersStats`
- **구조:**
```java
public class OrdersStats {
    private final long totalOrders; // 총 주문 수
    private final long pendingPaymentOrders; // 결제 대기 중인 주문 수
    private final long paidOrders; // 결제 완료된 주문 수
    private final long preparingShipmentOrders; // 배송 준비 중인 주문 수
    private final long shippedOrders; // 배송 중인 주문 수
    private final long deliveredOrders; // 배송 완료된 주문 수
    private final long confirmedOrders; // 구매 확정된 주문 수
    private final long cancelRequestedOrders; // 취소 요청된 주문 수
    private final long cancelledOrders; // 취소된 주문 수
    private final long refundRequestedOrders; // 환불 요청된 주문 수
    private final long refundedOrders; // 환불 완료된 주문 수
}
```

---

## 4. 판매 관리 대시보드 조회

### 요청
- **HTTP 메서드:** `GET`
- **경로:** `/api/dashboard/sales`
- **설명:** 판매 관련 통계 데이터를 반환합니다.

### 응답
- **타입:** `SalesStats`
- **구조:**
```java
public class SalesStats {
    private final long totalSalesProducts; // 총 판매된 상품 수
    private final BigDecimal totalSoldAmount; // 총 판매 금액
}
```

---

## 5. 입찰 관리 대시보드 조회

### 요청
- **HTTP 메서드:** `GET`
- **경로:** `/api/dashboard/bids`
- **설명:** 입찰 관련 통계 데이터를 반환합니다.

### 응답
- **타입:** `BidStats`
- **구조:**
```java
public class BidStats {
    private final long totalBids; // 총 입찰 수
    private final long bidsOnActiveAuctions; // 활성 경매의 입찰 수
    private final long bidsToday; // 오늘 입찰 수
}
```

---

## 6. 검수 관리 대시보드 조회

### 요청
- **HTTP 메서드:** `GET`
- **경로:** `/api/dashboard/inspections`
- **설명:** 검수 관련 통계 데이터를 반환합니다.

### 응답
- **타입:** `InspectionStats`
- **구조:**
```java
public class InspectionStats {
    private final long totalInspections; // 총 검수 건수
    private final long draftInspections; // 임시 저장된 검수 건수
    private final long submittedInspections; // 제출된 검수 건수
    private final long onlineApprovedInspections; // 온라인 승인된 검수 건수
    private final long onlineRejectedInspections; // 온라인 거부된 검수 건수
    private final long offlineInspectingInspections; // 오프라인 검수 중인 건수
    private final long offlineRejectedInspections; // 오프라인 거부된 검수 건수
    private final long completedInspections; // 완료된 검수 건수
}
```

---

## 7. 카탈로그 관리 대시보드 조회

### 요청
- **HTTP 메서드:** `GET`
- **경로:** `/api/dashboard/catalogs-overview`
- **설명:** 카탈로그 관련 통계 데이터를 반환합니다.

### 응답
- **타입:** `CatalogDashboardStats`
- **구조:**
```java
public class CatalogDashboardStats {
    private final CatalogStats catalogStats; // 카탈로그 통계
    private final ChecklistStats checklistStats; // 체크리스트 통계
}
```
- **`CatalogStats` 구조:**
```java
public class CatalogStats {
    private final long totalArtists; // 총 아티스트 수
    private final long pendingArtists; // 승인 대기 중인 아티스트 수
    private final long approvedArtists; // 승인된 아티스트 수
    private final long rejectedArtists; // 거부된 아티스트 수
    private final long totalAlbums; // 총 앨범 수
    private final long totalGoodsCategories; // 총 상품 카테고리 수
}
```
- **`ChecklistStats` 구조:**
```java
public class ChecklistStats {
    private final long totalChecklistTemplates; // 총 체크리스트 템플릿 수
    private final long totalChecklistItems; // 총 체크리스트 항목 수
}
```

---

## 8. CS 관리 대시보드 조회

### 요청
- **HTTP 메서드:** `GET`
- **경로:** `/api/dashboard/cs`
- **설명:** 고객 서비스 관련 통계 데이터를 반환합니다.

### 응답
- **타입:** `CSDashboardStats`
- **구조:**
```java
public class CSDashboardStats {
    private final NoticeStats noticeStats; // 공지사항 통계
    private final FaqStats faqStats; // FAQ 통계
    private final InquiryStats inquiryStats; // 문의 통계
}
```
- **`NoticeStats` 구조:**
```java
public class NoticeStats {
    private final long totalNotices; // 총 공지사항 수
    private final long draftNotices; // 임시 저장된 공지사항 수
    private final long activeNotices; // 활성 공지사항 수
    private final long pinnedNotices; // 고정된 공지사항 수
    private final long inactiveNotices; // 비활성 공지사항 수
}
```
- **`FaqStats` 구조:**
```java
public class FaqStats {
    private final long totalFaqs; // 총 FAQ 수
    private final long draftFaqs; // 임시 저장된 FAQ 수
    private final long activeFaqs; // 활성 FAQ 수
    private final long pinnedFaqs; // 고정된 FAQ 수
    private final long inactiveFaqs; // 비활성 FAQ 수
}
```
- **`InquiryStats` 구조:**
```java
public class InquiryStats {
    private final long totalInquiries; // 총 문의 수
    private final long pendingInquiries; // 답변 대기 중인 문의 수
    private final long inProgressInquiries; // 처리 중인 문의 수
    private final long onHoldInquiries; // 보류 중인 문의 수
    private final long rejectedInquiries; // 거부된 문의 수
    private final long answeredInquiries; // 답변 완료된 문의 수
    private final long todayInquiries; // 오늘 접수된 문의 수
}
```

---

## 9. 재무/운영 관리 대시보드 조회

### 요청
- **HTTP 메서드:** `GET`
- **경로:** `/api/dashboard/finance-operations`
- **설명:** 재무 및 운영 관련 통계 데이터를 반환합니다.

### 응답
- **타입:** `FinanceOperationsDashboardStats`
- **구조:**
```java
public class FinanceOperationsDashboardStats {
    private final SettlementStats settlementStats; // 정산 통계
    private final RefundStats refundStats; // 환불 통계
}
```
- **`SettlementStats` 구조:**
```java
public class SettlementStats {
    private final long totalSettlements; // 총 정산 건수
    private final long pendingSettlements; // 미정산 건수
    private final long paidSettlements; // 정산 완료 건수
    private final long cancelledSettlements; // 취소된 정산 건수
    private final long failedSettlements; // 실패한 정산 건수
}
```
- **`RefundStats` 구조:**
```java
public class RefundStats {
    private final long totalRefunds; // 총 환불 건수
    private final long requestedRefunds; // 환불 요청 건수
    private final long inTransitRefunds; // 환불 처리 중인 건수
    private final long inspectingRefunds; // 환불 검수 중인 건수
    private final long approvedRefunds; // 승인된 환불 건수
    private final long rejectedRefunds; // 거부된 환불 건수
    private final long completedRefunds; // 완료된 환불 건수
    private final long userCancelledRefunds; // 사용자 취소 환불 건수
}
