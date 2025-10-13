package com.eneifour.fantry.inspection.service;

import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.eneifour.fantry.mail.model.MailService;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.exception.MemberErrorCode;
import com.eneifour.fantry.member.exception.MemberException;
import com.eneifour.fantry.member.repository.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final MailService mailService;
    private final JpaMemberRepository memberRepository;

    @Async
    public void sendInspectionResultMail(ProductInspection inspection) {
        Member member = memberRepository.findById(inspection.getMemberId()).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        if(member == null || member.getEmail() == null) {
            log.error("검수 결과 알림을 보낼 회원 정보가 없습니다. memberId={}", inspection.getMemberId());
            return;
        }

        String to = member.getEmail();
        String statusLabel = inspection.getInspectionStatus().getLabel();
        String subject = "[Fantry] 요청하신 '" + inspection.getItemName() + "' 상품의 검수 결과 안내";
        String content = createEmailContent(member.getName(), inspection, statusLabel);

        try {
            mailService.sendEmail(to, subject, content, null, null);
            log.info("검수 결과 이메일 발송 성공. To: {}, Status: {}", to, statusLabel);
        } catch (Exception e) {
            log.error("검수 결과 이메일 발송에 실패했습니다. To: {}, Error: {}", to, e.getMessage());
            // TODO: 추후 DB 로그 테이블에 실패 기록을 저장하고, 스케줄러로 재발송하는 로직 추가 가능
        }
    }

    /**
     * 검수 상태에 따라 동적으로 이메일 본문을 생성합니다.
     */
    private String createEmailContent(String memberName, ProductInspection inspection, String statusLabel) {
        StringBuilder content = new StringBuilder();
        content.append("안녕하세요, ").append(memberName).append(" 님.<br>");
        content.append("요청하신 상품 '<b>").append(inspection.getItemName()).append("</b>'에 대한 ");
        content.append("<b>").append(statusLabel).append("</b> 결과를 안내해 드립니다.<br><br>");

        switch (inspection.getInspectionStatus()) {
            case ONLINE_APPROVED:
                content.append("1차 온라인 검수가 승인되었습니다. 상품을 Fantry로 발송해주세요.<br>");
                break;
            case ONLINE_REJECTED:
                content.append("아쉽지만 1차 온라인 검수가 반려되었습니다.<br>");
                content.append("<b>반려 사유:</b> ").append(inspection.getFirstRejectionReason()).append("<br>");
                break;
            case COMPLETED:
                content.append("최종 검수가 완료되어 상품 판매가 시작됩니다.<br>");
                break;
            case OFFLINE_REJECTED:
                content.append("아쉽지만 최종 오프라인 검수에서 반려되었습니다. 상품은 곧 반송될 예정입니다.<br>");
                content.append("<b>반려 사유:</b> ").append(inspection.getSecondRejectionReason()).append("<br>");
                break;
            default:
                content.append("자세한 내용은 Fantry 웹사이트 '마이페이지 - 나의 검수 현황' 메뉴에서 확인해주세요.<br>");
        }

        content.append("<br>감사합니다.<br>Fantry 팀 드림");
        return content.toString();
    }
}
