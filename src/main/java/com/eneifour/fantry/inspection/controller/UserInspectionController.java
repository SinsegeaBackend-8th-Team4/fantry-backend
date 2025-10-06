package com.eneifour.fantry.inspection.controller;

import com.eneifour.fantry.inspection.dto.InspectionRequest;
import com.eneifour.fantry.inspection.service.InspectionService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 유저용 검수 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/inspections")
@RequiredArgsConstructor
public class UserInspectionController {
    private final InspectionService inspectionService;

    /**
     * 1차 검수 신청 생성
     */
    @PostMapping
    public InspectionApiResponse<Integer> createInspection(
            @RequestPart("request") InspectionRequest request,
            @RequestPart("files") List<MultipartFile> files) {

        int memberId = 1; // TODO : 인증
        int inspectionID = inspectionService.createInspection(memberId, request, files);

        return InspectionApiResponse.ok(inspectionID);
    }
}
