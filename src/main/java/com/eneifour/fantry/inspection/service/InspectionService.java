package com.eneifour.fantry.inspection.service;

import com.eneifour.fantry.checklist.domain.ChecklistItem;
import com.eneifour.fantry.checklist.domain.ChecklistTemplate;
import com.eneifour.fantry.checklist.repository.ChecklistItemRepository;
import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.inspection.domain.InspectionFile;
import com.eneifour.fantry.inspection.domain.ProductChecklistAnswer;
import com.eneifour.fantry.inspection.domain.ProductInspection;
import com.eneifour.fantry.inspection.dto.InspectionRequestDto;
import com.eneifour.fantry.inspection.repository.InspectionFileRepository;
import com.eneifour.fantry.inspection.repository.InspectionRepository;
import com.eneifour.fantry.inspection.repository.ProductChecklistAnswerRepository;
import com.eneifour.fantry.inspection.support.exception.BusinessException;
import com.eneifour.fantry.inspection.support.exception.InspectionErrorCode;
import com.eneifour.fantry.member.domain.Member;
import com.eneifour.fantry.member.exception.MemberErrorCode;
import com.eneifour.fantry.member.exception.MemberException;
import com.eneifour.fantry.member.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final InspectionFileRepository inspectionFileRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final ProductChecklistAnswerRepository productChecklistAnswerRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public int createInspection(int memberId, InspectionRequestDto requestDto, List<MultipartFile> files) {
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
}
