package com.eneifour.fantry.inspection.service;

import com.eneifour.fantry.checklist.domain.ChecklistItem;
import com.eneifour.fantry.checklist.domain.ChecklistTemplate;
import com.eneifour.fantry.checklist.repository.ChecklistItemRepository;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.inspection.domain.InspectionFile;
import com.eneifour.fantry.inspection.domain.InspectionStatus;
import com.eneifour.fantry.inspection.domain.ProductChecklistAnswer;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.eneifour.fantry.inspection.dto.InspectionDetailResponse;
import com.eneifour.fantry.inspection.dto.InspectionListResponse;
import com.eneifour.fantry.inspection.dto.InspectionRejectRequest;
import com.eneifour.fantry.inspection.dto.InspectionRequest;
import com.eneifour.fantry.inspection.repository.InspectionFileRepository;
import com.eneifour.fantry.inspection.repository.InspectionRepository;
import com.eneifour.fantry.inspection.repository.ProductChecklistAnswerRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final InspectionFileRepository inspectionFileRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final ProductChecklistAnswerRepository productChecklistAnswerRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 1차 온라인 검수 신청
     * @param memberId 작성자
     * @param requestDto 검수 신청 데이터
     * @param files 검수 파일 데이터
     * @return 검수 ID
     */
    @Transactional
    public int createInspection(int memberId, InspectionRequest requestDto, List<MultipartFile> files) {
        // 파일 유효성 검증
        if (files == null || files.isEmpty()) {
            throw new BusinessException(InspectionErrorCode.EMPTY_FILE_ATTACHED);
        }

        // 1. ProductInspection Entity 생성 및 저장
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        // 엔티티 생성
        ProductInspection inspection = requestDto.toEntity(member.getMemberId());
        // DB 저장
        ProductInspection savedInspection = inspectionRepository.save(inspection);

        // 2. ProductChecklistAnswer Entity 생성 및 저장
        if(requestDto.getAnswers() != null) {
            requestDto.getAnswers().forEach((k,v) ->{
                // repo에서 itemLabel 조회
                String label = checklistItemRepository.findByItemKey(k).map(ChecklistItem::getLabel).orElse("N/A");
                // 복합키 생성
                ProductChecklistAnswer.Id answerId = new ProductChecklistAnswer.Id(savedInspection.getProductInspectionId(), ChecklistTemplate.Role.SELLER, k);
                
                String jsonAnswerValue;
                // 문자열 -> JSON 문자열
                try { jsonAnswerValue = objectMapper.writeValueAsString(v); }
                catch (JsonProcessingException e) { jsonAnswerValue = "\"\""; }

                // 엔티티 생성
                ProductChecklistAnswer answer = ProductChecklistAnswer.builder()
                        .id(answerId)
                        .productInspection(savedInspection)
                        .itemLabel(label)
                        .answerValue(jsonAnswerValue)
                        .build();

                // DB 저장
                productChecklistAnswerRepository.save(answer);
            });
        }

        // 파일 업로드
        String subDirectory = "ins/online";
        List<FileMeta> uploadedFileMetaList = fileService.uploadFiles(files, subDirectory, member);
        
        // InspectionFile Entity 생성 및 저장
        uploadedFileMetaList.forEach(fileMeta -> {
            InspectionFile inspectionFile = InspectionFile.builder()
                    .productInspection(savedInspection)
                    .fileMeta(fileMeta)
                    .build();
            inspectionFileRepository.save(inspectionFile);
        });
        
        // 검수 ID 반환
        return savedInspection.getProductInspectionId();
    }

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
     * 검수 상세 정보 조회
     * @param productInspectionId 검수 ID
     * @return 검수 상세 정보 DTO
     */
    public InspectionDetailResponse getInspectionDetail(int productInspectionId){
        // 1. 기본 상세 정보 조회
        ProductInspection inspection = findInspectionById(productInspectionId); // 없을 경우 예외 처리

        InspectionDetailResponse response = inspectionRepository.findInspectionDetailById(inspection.getProductInspectionId())
                .orElseThrow(() -> new BusinessException(InspectionErrorCode.INSPECTION_NOT_FOUND));

        // 2. 파일 목록 조회 및 설정
        List<InspectionDetailResponse.FileInfo> files = inspectionRepository.findFilesById(productInspectionId);
        response.setFiles(files);

        // 3. 체크리스트 답변 목록 조회 및 설정
        List<ProductChecklistAnswer> answers = productChecklistAnswerRepository.findByProductInspection_ProductInspectionId(productInspectionId);
        List<InspectionDetailResponse.ChecklistAnswerInfo> answerInfos = answers.stream()
                .map(answer -> InspectionDetailResponse.ChecklistAnswerInfo.builder()
                        .itemKey(answer.getId().getItemKey())
                        .itemLabel(answer.getItemLabel())
                        .answerValue(answer.getAnswerValue())
                        .build())
                .collect(Collectors.toList());
        response.setAnswers(answerInfos);

        return response;
    }

    /**
     * 1차 검수 승인
     */
    @Transactional
    public void approveFirstInspection(int productInspectionId){
        ProductInspection inspection = findInspectionById(productInspectionId);

        // 검수 상태가 SUBMITTED 아닐 경우 예외 처리
        if (inspection.getInspectionStatus() != InspectionStatus.SUBMITTED) {
            throw new BusinessException(InspectionErrorCode.NOT_SUBMITTED_STATUS_FOR_FIRST_INSPECTION);
        }

        inspection.setInspectionStatus(InspectionStatus.FIRST_REVIEWED);
        inspection.setOnlineInspectedAt(LocalDateTime.now());
        inspection.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * 1차 검수 반려
     */
    @Transactional
    public void rejectFirstInspection(int productInspectionId, InspectionRejectRequest request) {
        ProductInspection inspection = findInspectionById(productInspectionId);

        // 검수 상태가 SUBMITTED 아닐 경우 예외 처리
        if (inspection.getInspectionStatus() != InspectionStatus.SUBMITTED) {
            throw new BusinessException(InspectionErrorCode.NOT_SUBMITTED_STATUS_FOR_FIRST_INSPECTION);
        }

        inspection.setInspectionStatus(InspectionStatus.FIRST_REJECTED);
        inspection.setFirstRejectionReason(request.getRejectionReason());
        inspection.setOnlineInspectedAt(LocalDateTime.now());
        inspection.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * 검수 ID로 검수 조회
     */
    private ProductInspection findInspectionById(int productInspectionId){
        return inspectionRepository.findById(productInspectionId).orElseThrow(() -> new BusinessException(InspectionErrorCode.INSPECTION_NOT_FOUND));
    }
}
