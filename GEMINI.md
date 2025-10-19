# 부하 테스트 가이드라인 (대규모 및 동시성 중심)

이 문서는 프로젝트 발표를 위한 부하 테스트 시나리오 및 K6 활용 방안을 요약합니다. 특히 '대규모' 트래픽과 '동시성' 처리에 중점을 둡니다.

---

## 1. 핵심 부하 테스트 시나리오

### 1.1. 경매 입찰 (WebSocket) - 가장 중요

*   **목표:** 다수의 사용자가 동시에 경매에 입찰할 때 시스템의 동시성 처리 능력, 응답 시간, 오류율을 측정합니다. 경매 마감 직전과 같이 입찰이 집중되는 상황을 시뮬레이션합니다.
*   **테스트 대상:** WebSocket 연결 및 메시지 전송 (`ws://[your-app-host]:[port]/ws` 및 `@MessageMapping("/auctions/{auctionId}/bids")`로 메시지 전송).
*   **K6 스크립트 고려사항:**
    *   K6의 `ws` 모듈을 사용하여 WebSocket 연결을 설정하고 메시지를 주고받습니다.
    *   각 가상 사용자(VU)가 고유한 `memberId`를 가지고 유효한 `auctionId`에 입찰하도록 데이터를 준비합니다.
    *   STOMP 프로토콜을 사용하는 경우, `CONNECT` 및 `SEND` 프레임을 적절히 구성해야 합니다.
    *   입찰 메시지 전송 후 서버의 응답을 확인하고, 성공적인 입찰에 대한 메트릭을 수집합니다.

### 1.2. 메인 페이지 / 경매 목록 조회 (동적 콘텐츠)

*   **목표:** 많은 사용자가 동시에 메인 페이지에 접속하여 동적으로 업데이트되는 경매 목록(예: 현재 최고 입찰가, 남은 시간)을 조회할 때의 성능을 테스트합니다.
*   **테스트 대상:** `GET /` (메인 페이지), `GET /api/auctions`, `GET /api/auctions/hotdeal`, `GET /api/auctions/{auctionId}`.
*   **K6 스크립트 고려사항:**
    *   `http.get()`을 사용하여 HTTP 요청을 보냅니다.
    *   다양한 검색 조건(`AuctionSearchCondition`)을 사용하여 실제 사용자 검색 패턴을 시뮬레이션합니다.
    *   응답 시간, 오류율, 처리량을 측정하고, 동적 데이터 로딩 및 캐싱 효율성을 평가합니다.

### 1.3. 경매 마감/정산 (API 트리거인 경우)

*   **목표:** 경매 종료 처리(낙찰자 결정, 정산 시작 등)가 API 호출에 의해 트리거되는 경우, 이 프로세스가 부하 상태에서 얼마나 효율적으로 작동하는지 테스트합니다.
*   **테스트 대상:** 관련 `PATCH` 또는 `POST` 엔드포인트 (예: `PATCH /api/auctions/{auctionId}/status/sold`).
*   **K6 스크립트 고려사항:**
    *   `http.patch()` 또는 `http.post()`를 사용하여 요청을 보냅니다.
    *   이 시나리오는 일반적으로 입찰만큼 높은 동시성을 요구하지 않지만, 트랜잭션의 정확성과 처리 시간을 확인하는 데 중요합니다.

---

## 2. 시스템 내 복잡한 쿼리 및 테스트 전략

시스템의 잠재적인 병목 현상을 파악하기 위해 다음 복잡한 쿼리들을 부하 테스트 시나리오에 포함하는 것을 권장합니다.

### 2.1. `DashboardRepository.getIntegratedDashboardData()`

*   **설명:** 여러 엔티티에 대한 다수의 `COUNT(*)` 서브쿼리를 포함하는 매우 복잡한 네이티브 SQL 쿼리입니다. 시스템 전반의 데이터를 집계합니다.
*   **트리거 API:** `GET /api/dashboard/integrated`
*   **테스트 전략:** 많은 사용자가 동시에 통합 대시보드를 요청하여 데이터베이스의 읽기 성능과 인덱싱 효율성을 테스트합니다.

### 2.2. `AuctionRepository.findByAuctionId()` / `findAuctionDetailsByProductInspectionIdOrderByCreatedAtDesc()`

*   **설명:** 여러 `JOIN`과 DTO 프로젝션을 포함하는 JPQL 쿼리로, 경매 상세 정보를 조회합니다.
*   **트리거 API:** `GET /api/auctions/{auctionId}`, `GET /api/auctions/inspection/{productInspectionId}`
*   **테스트 전략:** 많은 사용자가 동시에 다양한 경매의 상세 정보를 조회하여 복잡한 조인 및 DTO 매핑의 성능을 측정합니다.

### 2.3. `AuctionRepository.findActiveAuctionsBidByMember()`

*   **설명:** `JOIN` 및 `DISTINCT`를 사용하여 특정 회원이 입찰한 활성 경매 목록을 조회합니다.
*   **트리거 API:** `GET /api/auctions/member/{memberId}/bids`
*   **테스트 전략:** 많은 사용자가 자신의 입찰 내역을 조회할 때의 데이터베이스 성능을 테스트합니다.

### 2.4. `AuctionRepository.findHotDealAuctions()`

*   **설명:** `LEFT JOIN`, `GROUP BY`, `ORDER BY COUNT(b) DESC`를 포함하여 핫딜 경매 목록을 조회합니다. 입찰이 많은 경우 리소스 집약적일 수 있습니다.
*   **트리거 API:** `GET /api/auctions/hotdeal`
*   **테스트 전략:** 많은 사용자가 핫딜 경매 목록을 탐색할 때의 복잡한 집계 및 정렬 쿼리 성능을 테스트합니다.

### 2.5. `BidRepository.getBidStats()`

*   **설명:** `DashboardRepository.getIntegratedDashboardData()`와 유사하게 여러 `COUNT(*)` 서브쿼리를 포함하는 네이티브 SQL 쿼리로, 입찰 통계를 계산합니다.
*   **트리거 API:** `GET /api/dashboard/integrated` (간접적으로), 또는 입찰 통계를 직접 노출하는 API가 있다면 해당 API.
*   **테스트 전략:** 입찰 통계 조회 시 데이터베이스의 읽기 성능을 측정합니다.

### 2.6. `InspectionRepository`의 복잡한 쿼리들

*   **설명:** `findAllByInspectionStatusIn()`, `findInspectionDetailById()`, `findMyInspectionsByMemberId()` 등은 여러 `JOIN`과 DTO 프로젝션을 사용하여 검수 목록 및 상세 정보를 조회합니다.
*   **트리거 API:** `GET /api/inspections`, `GET /api/inspections/{id}`, `GET /api/inspections/member/{memberId}`
*   **테스트 전략:** 관리자 또는 회원이 검수 관련 정보를 조회할 때의 성능을 테스트합니다.

### 2.7. `ReturnRepository.findWithAttachmentsAndHistoriesById()`

*   **설명:** `@EntityGraph`를 사용하여 환불/반품 요청의 모든 관련 엔티티(주문, 회원, 첨부파일, 상태 이력 등)를 깊게 페치하는 쿼리입니다.
*   **트리거 API:** 환불/반품 요청 상세 조회 API (예: `GET /api/refunds/{id}`)
*   **테스트 전략:** 깊은 엔티티 그래프 로딩 시의 성능을 측정하고 N+1 문제 발생 여부를 확인합니다.

---

## 3. K6를 활용한 성능 저하 테스트 방법

1.  **기준선 테스트 (Baseline Test):** 각 시나리오를 적은 수의 가상 사용자(예: 1-5 VU)로 실행하여 정상 상태에서의 응답 시간을 측정합니다.
2.  **램프업 테스트 (Ramp-up Test):** 가상 사용자 수를 점진적으로 늘려가면서 시스템의 응답 시간, 오류율, 처리량 및 리소스 사용률(CPU, 메모리, DB 연결) 변화를 관찰합니다.
3.  **스트레스 테스트 (Stress Test):** 시스템의 예상 용량을 초과하는 부하를 주어 시스템의 한계점과 장애 지점을 파악합니다.
4.  **소크 테스트 (Soak Test / Endurance Test):** 중간 정도의 부하로 장시간(예: 몇 시간) 테스트를 실행하여 메모리 누수, 리소스 고갈 또는 기타 장기적인 성능 저하 문제를 확인합니다.

**일반적인 K6 모범 사례:**

*   **K6 설치:** 리눅스 서버에 K6를 직접 설치합니다.
*   **대상 호스트:** K6 스크립트에서 `http://localhost:8080` 또는 애플리케이션 서버의 내부 IP 주소를 사용합니다.
*   **모니터링:** 테스트 실행 중 서버의 CPU, RAM, 디스크 I/O, 네트워크 I/O 및 데이터베이스 성능(쿼리 시간, 연결 풀 사용량)을 실시간으로 모니터링합니다.
*   **테스트 데이터:** 현실적인 테스트 데이터를 충분히 준비하여 실제 환경과 유사한 조건을 만듭니다.
*   **어설션/체크:** `check()` 함수를 사용하여 HTTP 상태 코드, 응답 본문 내용 등을 검증하여 응답의 정확성을 확인합니다.

이 가이드라인이 부하 테스트 계획에 도움이 되기를 바랍니다.