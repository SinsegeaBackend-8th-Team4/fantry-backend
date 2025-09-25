package com.eneifour.fantry.common.util.file;

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
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class TestController {
    private final FileService fileService;

    private final String subDirectory = "cs/inquiry";

    @PostMapping("/upload")
    public ResponseEntity<List<FileMeta>> uploadFiles(@RequestPart List<MultipartFile> files) {
        log.warn("요청들어옴");
        Member member = new Member();
        member.setMemberId(1);
        member.setName("testUser");

        List<FileMeta> uploadedFileMeta = fileService.uploadFiles(files, subDirectory, member);
        if (uploadedFileMeta.isEmpty() && !files.isEmpty()) {
            // 모든 파일 업로드가 실패했지만, 요청 자체는 유효했던 경우
            // 이 경우는 FileException이 아닌, 서비스 로직의 결과이므로 컨트롤러에서 처리
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "모든 파일 업로드에 실패했습니다.");
        }
        return new ResponseEntity<>(uploadedFileMeta, HttpStatus.CREATED);
    }

    @GetMapping("/{fileMetaId}")
    public ResponseEntity<String> getFileAccessUrl(@PathVariable int fileMetaId) {
        String fileUrl = fileService.getFileAccessUrl(fileMetaId);
        return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    }

    @DeleteMapping("/{fileMetaId}")
    public ResponseEntity<String> deleteFile(@PathVariable int fileMetaId) {
        Member member = new Member();
        member.setMemberId(1);
        member.setName("테스트");

        fileService.deleteFile(fileMetaId, member);
        return new ResponseEntity<>("파일이 성공적으로 삭제 처리되었습니다.", HttpStatus.OK);
    }
}
