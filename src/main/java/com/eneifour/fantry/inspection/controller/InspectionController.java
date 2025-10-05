package com.eneifour.fantry.inspection.controller;

import com.eneifour.fantry.inspection.dto.InspectionRequestDto;
import com.eneifour.fantry.inspection.service.InspectionService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inspection")
@RequiredArgsConstructor
public class InspectionController {
    private final InspectionService inspectionService;

    @PostMapping
    public ResponseEntity<InspectionApiResponse<Integer>> createInspection(
            @RequestPart("request")InspectionRequestDto inspectionRequestDto,
            @RequestPart("files") List<MultipartFile> files) {

        int memberId = 1; // 임시
        int inspectionID = inspectionService.createInspection(memberId, inspectionRequestDto, files);

        return ResponseEntity.status(HttpStatus.CREATED).body(InspectionApiResponse.ok(inspectionID));
    }
}
