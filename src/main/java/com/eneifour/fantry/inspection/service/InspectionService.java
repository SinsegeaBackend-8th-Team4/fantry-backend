package com.eneifour.fantry.inspection.service;

import com.eneifour.fantry.catalog.domain.GroupType;
import com.eneifour.fantry.checklist.domain.ChecklistItem;
import com.eneifour.fantry.checklist.domain.ChecklistTemplate;
import com.eneifour.fantry.checklist.dto.OfflineChecklistItemResponse;
import com.eneifour.fantry.checklist.dto.OnlineChecklistItemResponse;
import com.eneifour.fantry.checklist.repository.ChecklistItemRepository;
import com.eneifour.fantry.checklist.repository.ProductChecklistAnswerRepository;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.inspection.domain.*;
import com.eneifour.fantry.inspection.dto.*;
import com.eneifour.fantry.inspection.repository.InspectionFileRepository;
import com.eneifour.fantry.inspection.repository.InspectionRepository;
import com.eneifour.fantry.inspection.support.api.InspectionPageResponse;
import com.eneifour.fantry.inspection.support.exception.BusinessException;
import com.eneifour.fantry.inspection.support.exception.InspectionErrorCode;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.exception.MemberErrorCode;
import com.eneifour.fantry.member.exception.MemberException;
import com.eneifour.fantry.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InspectionService {
    // ========== [ 공통 의존성 / 헬퍼 ] ==========
    private final InspectionRepository inspectionRepository;
    private final InspectionFileRepository inspectionFileRepository;
    private final ProductChecklistAnswerRepository productChecklistAnswerRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationService notificationService;

    //  == 공통 헬퍼 ==
    /** 검수 ID로 검수 조회 */
    private ProductInspection findInspectionById(int productInspectionId){
        return inspectionRepository.findById(productInspectionId).orElseThrow(() -> new BusinessException(InspectionErrorCode.INSPECTION_NOT_FOUND));
    }

    /** 검수 상태 체크 */
    private void validateInspectionStatus(ProductInspection inspection, InspectionStatus expected, InspectionErrorCode errorCode) {
        if (inspection.getInspectionStatus() != expected) {
            throw new BusinessException(errorCode);
        }
    }

    /** 검수 수정 시간 세팅 */
    private void updateTimestamps(ProductInspection inspection) {
        inspection.setUpdatedAt(LocalDateTime.now());
    }

    /** 검수자 답변 저장 */
    private void saveInspectorAnswers(ProductInspection inspection, List<OfflineInspectionApproveRequest.InspectorAnswerDto> answers) {
        if (answers != null && !answers.isEmpty()) {
            answers.forEach(dto -> {
                String label = checklistItemRepository.findByItemKey(dto.getItemKey()).map(ChecklistItem::getLabel).orElse("N/A");
                ProductChecklistAnswer.Id answerId = new ProductChecklistAnswer.Id(inspection.getProductInspectionId(), ChecklistTemplate.Role.INSPECTOR, dto.getItemKey());

                String jsonAnswerValue;
                try {
                    jsonAnswerValue = objectMapper.writeValueAsString(dto.getAnswerValue());
                } catch (JsonProcessingException e) {
                    jsonAnswerValue = "\"\"";
                }

                ProductChecklistAnswer answer = ProductChecklistAnswer.builder()
                        .id(answerId)
                        .productInspection(inspection)
                        .itemLabel(label)
                        .answerValue(jsonAnswerValue)
                        .note(dto.getNote())
                        .build();
                productChecklistAnswerRepository.save(answer);
            });
        }
    }

    // ========== [ 검수 공통 ] ==========
    /**
     * 검수 상태 별 페이지 조회
     * @param statuses 조회할 검수 상태 목록 (e.g. ?statuses=SUBMITTED,FIRST_REVIEWED)
     * @param pageable 페이지네이션, 정렬 정보 (e.g. ?page=0&size=20&sort=submittedAt,desc)
     * @return 페이징 처리된 검수 목록
     */
    public InspectionPageResponse<InspectionListResponse> getInspectionsByStatuses(List<InspectionStatus> statuses, Pageable pageable){
        Page<InspectionListResponse> page = inspectionRepository.findAllByInspectionStatusIn(statuses, pageable);
        return InspectionPageResponse.fromPage(page);
    }

    /**
     *  재고 상태 별 페이지 조회
     * @param statuses 조회할 재고 상태 목록 (e.g. ?statuses=PENDING , SOLD)
     * @param pageable 페이지네이션, 정렬 정보 (e.g. ?page=0&size=20&sort=submittedAt,desc)
     * @return 페이징 처리된 검수 목록
     */
    public InspectionPageResponse<InventoryListResponse> getInspectionsByInventoryStatuses(List<InventoryStatus> statuses, Pageable pageable){
        Page<InventoryListResponse> page = inspectionRepository.findAllByInventoryStatusIn(statuses, pageable);
        return InspectionPageResponse.fromPage(page);
    }

    // ========== [ 1차 검수 (Online) ] ==========
    /** 1차 온라인 검수 신청 */
    @Transactional
    public int createInspection(int memberId, InspectionRequest requestDto, List<MultipartFile> files) {
        // 1. 파일 유효성 검증
        if (files == null || files.isEmpty()) {
            throw new BusinessException(InspectionErrorCode.EMPTY_FILE_ATTACHED);
        }
        // 2. 판매자 엔티티 생성
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        // 3. 기본 검수 엔티티 생성 및 DB 저장
        ProductInspection inspection = requestDto.toEntity(member.getMemberId());
        ProductInspection savedInspection = inspectionRepository.save(inspection);
        // 4. 판매자 체크리스트 답변 DB 저장
        if (requestDto.getAnswers() != null && !requestDto.getAnswers().isEmpty()) {
            requestDto.getAnswers().forEach((k,v) -> {
                // 4-1. 체크리스트 항목명(label) 조회
                String label = checklistItemRepository.findByItemKey(k).map(ChecklistItem::getLabel).orElse("N/A");
                // 4-2. 체크리스트 답변 복합키 생성 (검수ID, 역할, 항목 키)
                ProductChecklistAnswer.Id answerId = new ProductChecklistAnswer.Id(savedInspection.getProductInspectionId(), ChecklistTemplate.Role.SELLER, k);
                // 4-3. 판매자 답변 JSON 변환
                String jsonAnswerValue;
                try { jsonAnswerValue = objectMapper.writeValueAsString(v); }
                catch (JsonProcessingException e) { jsonAnswerValue = "\"\""; } // 변환 실패 시 빈 문자열
                // 4-4. 답변 엔티티 생성 및 DB 저장
                ProductChecklistAnswer answer = ProductChecklistAnswer.builder()
                        .id(answerId)
                        .productInspection(savedInspection)
                        .itemLabel(label)
                        .answerValue(jsonAnswerValue)
                        .build();
                productChecklistAnswerRepository.save(answer);
            });
        }
        // 5. 검수 이미지 업로드 및 파일 메타 저장
        String subDirectory = "ins/online";
        List<FileMeta> uploadedFileMetaList = fileService.uploadFiles(files, subDirectory, member);
        
        uploadedFileMetaList.forEach(fileMeta -> {
            InspectionFile inspectionFile = InspectionFile.builder()
                    .productInspection(savedInspection)
                    .fileMeta(fileMeta)
                    .build();
            inspectionFileRepository.save(inspectionFile);
        });
        
        // 6. 검수 ID 반환
        return savedInspection.getProductInspectionId();
    }


    /** 1차 온라인 검수 상세 정보 조회 */
    public OnlineInspectionDetailResponse getOnlineInspectionDetail(int productInspectionId){
        // 1. 기본 상세 정보 조회
        ProductInspection inspection = findInspectionById(productInspectionId); // 없을 경우 예외 처리

        OnlineInspectionDetailResponse response = inspectionRepository.findInspectionDetailById(inspection.getProductInspectionId())
                .orElseThrow(() -> new BusinessException(InspectionErrorCode.INSPECTION_NOT_FOUND));

        // 2. 파일 목록 조회 및 설정
        List<OnlineInspectionDetailResponse.FileInfo> files = inspectionRepository.findFilesById(productInspectionId);
        response.setFiles(files);

        // 3. 체크리스트 답변 목록 조회 및 설정
        List<ProductChecklistAnswer> answers = productChecklistAnswerRepository.findByProductInspection_ProductInspectionIdAndId_ChecklistRole(productInspectionId, ChecklistTemplate.Role.SELLER);
        List<OnlineInspectionDetailResponse.ChecklistAnswerInfo> answerInfos = answers.stream()
                .map(answer -> OnlineInspectionDetailResponse.ChecklistAnswerInfo.builder()
                        .itemKey(answer.getId().getItemKey())
                        .itemLabel(answer.getItemLabel())
                        .answerValue(answer.getAnswerValue())
                        .build())
                .collect(Collectors.toList());
        response.setAnswers(answerInfos);

        return response;
    }

    /** 1차 검수 승인 */
    @Transactional
    public void approveFirstInspection(int productInspectionId, int firstInspectorId){
        ProductInspection inspection = findInspectionById(productInspectionId);
        validateInspectionStatus(inspection, InspectionStatus.SUBMITTED,InspectionErrorCode.NOT_SUBMITTED_STATUS_FOR_FIRST_INSPECTION); // 검수 상태가 SUBMITTED 아닐 경우 예외 처리

        inspection.setFirstInspectorId(firstInspectorId);
        inspection.setInspectionStatus(InspectionStatus.ONLINE_APPROVED);
        inspection.setOnlineInspectedAt(LocalDateTime.now());
        updateTimestamps(inspection);

        // 결과 이메일 발송
        notificationService.sendInspectionResultMail(inspection);
    }

    /** 1차 검수 반려 */
    @Transactional
    public void rejectFirstInspection(int productInspectionId, int firstInspectorId, InspectionRejectRequest request){
        ProductInspection inspection = findInspectionById(productInspectionId);
        validateInspectionStatus(inspection, InspectionStatus.SUBMITTED,InspectionErrorCode.NOT_SUBMITTED_STATUS_FOR_FIRST_INSPECTION);

        inspection.setFirstInspectorId(firstInspectorId);
        inspection.setInspectionStatus(InspectionStatus.ONLINE_REJECTED);
        inspection.setFirstRejectionReason(request.getRejectionReason());
        inspection.setOnlineInspectedAt(LocalDateTime.now());
        updateTimestamps(inspection);

        // 결과 이메일 발송
        notificationService.sendInspectionResultMail(inspection);
    }

    // ========== [ 2차 검수 (Offline) ] ==========
    /** 2차 오프라인 검수 상세 조회 */
    public OfflineInspectionDetailResponse getOfflineInspectionDetail(int productInspectionId){
        // 1. 기본 검수 엔티티 조회
        ProductInspection inspection = findInspectionById(productInspectionId);
        // 1-1. 2차 검수자 정보 조회
        OnlineInspectionDetailResponse.UserInfo secondInspectorInfo = null;
        if(inspection.getSecondInspectorId() != null){
            secondInspectorInfo = memberRepository.findById(inspection.getSecondInspectorId()).map(
                inspector -> new OnlineInspectionDetailResponse.UserInfo(
                        inspector.getMemberId(), inspector.getName(), inspector.getEmail(), inspector.getTel())
            ).orElse(null);
        }

        // 2. 판매자 체크리스트 답변 조회 및 매핑
        List<ProductChecklistAnswer> sellerAnswers = productChecklistAnswerRepository.findByProductInspection_ProductInspectionIdAndId_ChecklistRole(productInspectionId, ChecklistTemplate.Role.SELLER);
        Map<String, String> sellerAnswerMap = sellerAnswers.stream().collect(Collectors.toMap(a -> a.getId().getItemKey(), ProductChecklistAnswer::getAnswerValue));
        // 3. 검수자 체크리스트 답변 조회 및 매핑
        List<ProductChecklistAnswer> inspectorAnswers = productChecklistAnswerRepository.findByProductInspection_ProductInspectionIdAndId_ChecklistRole(productInspectionId, ChecklistTemplate.Role.INSPECTOR);
        Map<String, String> inspectorAnswerMap = inspectorAnswers.stream().collect(Collectors.toMap(a -> a.getId().getItemKey(), ProductChecklistAnswer::getAnswerValue));
        Map<String, String> inspectorNoteMap = inspectorAnswers.stream().filter(a -> a.getNote() != null).collect(Collectors.toMap(a -> a.getId().getItemKey(), ProductChecklistAnswer::getNote));

        // 4. 체크리스트 모든 항목 조회
        List<ChecklistItem> items = checklistItemRepository.findByTemplateIdAndCategoryId(inspection.getTemplateId(), inspection.getGoodsCategoryId());
        // 5. 판매자/검수자 답변 병합
        List<OfflineChecklistItemResponse> checklist = items.stream().map(item -> OfflineChecklistItemResponse.builder()
                        .checklistItem(OnlineChecklistItemResponse.from(item))
                        .sellerAnswer(sellerAnswerMap.get(item.getItemKey()))
                        .inspectorAnswer(inspectorAnswerMap.get(item.getItemKey()))
                        .note(inspectorNoteMap.get(item.getItemKey()))
                        .build()).toList();
        // 6. 응답 DTO 조립 및 반환
        return OfflineInspectionDetailResponse.builder()
                .onlineDetail(getOnlineInspectionDetail(inspection.getProductInspectionId()))
                .secondInspector(secondInspectorInfo)
                .checklist(checklist)
                .finalBuyPrice(inspection.getFinalBuyPrice())
                .priceDeductionReason(inspection.getPriceDeductionReason())
                .inspectionNotes(inspection.getInspectionNotes())
                .secondRejectionReason(inspection.getSecondRejectionReason())
                .build();
    }

    /** 2차 검수 승인 */
    @Transactional
    public void approveSecondInspection(int productInspectionId, int secondInspectorId, OfflineInspectionApproveRequest request) {
        ProductInspection inspection = findInspectionById(productInspectionId);
        validateInspectionStatus(inspection, InspectionStatus.OFFLINE_INSPECTING, InspectionErrorCode.NOT_FIRST_REVIEWED_STATUS_FOR_SECOND_INSPECTION); // 검수 상태가 SUBMITTED 아닐 경우 예외 처리

        // 검수자 답변 저장
        saveInspectorAnswers(inspection, request.getInspectorAnswers());

        // 검수 정보 업데이트
        inspection.setSecondInspectorId(secondInspectorId);
        inspection.setInspectionStatus(InspectionStatus.COMPLETED);
        inspection.setFinalBuyPrice(request.getFinalBuyPrice());
        inspection.setExpectedPrice(request.getExpectedPrice());
        inspection.setMarketAvgPrice(request.getMarketAvgPrice());
        inspection.setPriceDeductionReason(request.getPriceDeductionReason());
        inspection.setInspectionNotes(request.getInspectionNotes());
        inspection.setOfflineInspectedAt(LocalDateTime.now());
        inspection.setCompletedAt(LocalDateTime.now());
        updateTimestamps(inspection);

        // 결과 이메일 발송
        notificationService.sendInspectionResultMail(inspection);
    }

    /** 2차 검수 반려 */
    @Transactional
    public void rejectSecondInspection(int productInspectionId, int secondInspectorId, OfflineInspectionRejectRequest request) {
        ProductInspection inspection = findInspectionById(productInspectionId);
        validateInspectionStatus(inspection, InspectionStatus.OFFLINE_INSPECTING, InspectionErrorCode.NOT_FIRST_REVIEWED_STATUS_FOR_SECOND_INSPECTION);

        // 검수자 답변 저장
        saveInspectorAnswers(inspection, request.getInspectorAnswers());

        // 검수 정보 업데이트
        inspection.setSecondInspectorId(secondInspectorId);
        inspection.setInspectionStatus(InspectionStatus.OFFLINE_REJECTED);
        inspection.setSecondRejectionReason(request.getRejectionReason());
        inspection.setOfflineInspectedAt(LocalDateTime.now());
        updateTimestamps(inspection);

        // 결과 이메일 발송
        notificationService.sendInspectionResultMail(inspection);
    }

    /** 특정 회원의 모든 검수 현황 리스트 */
    public InspectionPageResponse<MyInspectionResponse> getMyInspections(int memberId, Pageable pageable) {
        Page<MyInspectionResponse> page = inspectionRepository.findMyInspectionsByMemberId(memberId, pageable);
        return InspectionPageResponse.fromPage(page);
    }

    /** 판매자 상품 발송 확인 후 상태 변경 */
    @Transactional
    public void startOfflineInspection(int productInspectionId, int memberId) {
        ProductInspection inspection = findInspectionById(productInspectionId);

        // 1. 본인 검수 인지 확인
        if(inspection.getMemberId() != memberId) throw new BusinessException(InspectionErrorCode.ACCESS_DENIED);
        // 2. 상태 검증 (1차 온라인 검수 승인(ONLINE_APPROVED) 상태가 아니면 예외 발생
        if (inspection.getInspectionStatus() != InspectionStatus.ONLINE_APPROVED) throw new BusinessException(InspectionErrorCode.CANNOT_START_OFFLINE_INSPECTION);
        // 3. 상태를 오프라인 검수 중(OFFLINE_INSPECTING) 으로 변경
        inspection.setInspectionStatus(InspectionStatus.OFFLINE_INSPECTING);
        updateTimestamps(inspection);
    }

    /**
     * 검수 ID로 재고 상태 변경
     * @param productInspectionId 변경할 검수 ID
     * @param status 새로운 재고 상태
     */
    @Transactional
    public void updateInventoryStatus(int productInspectionId, InventoryStatus status) {
        // 1. 검수 엔티티 조회
        ProductInspection inspection = findInspectionById(productInspectionId);
        // 2. 파라미터로 받은 status로 재고상태 변경
        inspection.setInventoryStatus(status);
        // 3. 수정 시간 업데이트
        updateTimestamps(inspection);

        log.info("검수 ID {}의 재고 상태가 {}로 업데이트되었습니다.", productInspectionId, status);
    }

    /**
     * 검수 ID로 등록한 아티스트 그룹 조회
     * @param inspectionId 검수 ID
     * @return 아티스트 그룹(ENUM)
     */
    public GroupType getGroupTypeById(int inspectionId) {
        Optional<GroupType> groupType = inspectionRepository.findGroupTypeById(inspectionId);
        return groupType.orElse(null);
    }
}
