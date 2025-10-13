package com.eneifour.fantry.refund.domain;

import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 환불/반품 요청의 상태 변경 이력을 기록하는 엔티티입니다.
 * 어떤 요청이, 언제, 누구에 의해, 어떻게 상태가 변경되었는지 추적합니다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name="return_status_history")
public class ReturnStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyId;

    // 이력이 속한 환불/반품 요청
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_request_id", nullable = false)
    private ReturnRequest returnRequest;

    // 변경 전 상태
    @Enumerated(EnumType.STRING)
    private ReturnStatus previousStatus;

    // 변경 후 새로운 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReturnStatus newStatus;

    // 상태를 변경한 주체 (주로 관리자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Member updatedBy;

    // 상태 변경 시간
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime updatedAt;

    /** 상태 변경과 관련된 메모 (예: 거절 사유 요약) */
    private String memo;
}
