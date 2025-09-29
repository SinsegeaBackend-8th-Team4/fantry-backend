package com.eneifour.fantry.inspection.controller;

import com.eneifour.fantry.common.util.file.FileMeta;
import com.eneifour.fantry.common.util.file.FileService;
import com.eneifour.fantry.inspection.service.InspectionService;
import com.eneifour.fantry.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inspection")
@RequiredArgsConstructor
public class InspectionController {
    private final InspectionService inspectionService;
    private final FileService fileService;

    private final String subDirectory = "ins/online"; // 1차 온라인 검수 파일 저장 위치

    @PostMapping("/files")
    public ResponseEntity<List<FileMeta>> uploadFiles(@RequestPart List<MultipartFile> files) {
        Member member = new Member();
        member.setMemberId(1);
        member.setName("testUser");

        List<FileMeta> uploadedFileMeta = fileService.uploadFiles(files, subDirectory, member);
        if (uploadedFileMeta.isEmpty() && !files.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "모든 파일 업로드에 실패했습니다.");
        }
        return new ResponseEntity<>(uploadedFileMeta, HttpStatus.CREATED);
    }
}
