package com.eneifour.fantry.common.util.file;

import com.eneifour.fantry.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 * 파일 업로드, 삭제, 조회 서비스
 * 조회(url 반환) : public String getFileAccessUrl(int fileMetaId)
 * 업로드 : List<FileMeta> uploadFiles(List<MultipartFile> files, String subDirectory, Member member)
 * - subDirectory는 저장될 세부 경로입니다. 매개변수는 아래 양식에 맞추어 도메인별 서비스 혹은 컨트롤러에서 호출합니다.
 *   subDirectory = "member/profile", "cs/inquiry"
 *
 * 저장경로:
 * - 1. 로컬 ->  C:/fantry_uploads
 *      BASE URL: =http://localhost:8080/static/
 * - 2. 서버 -> /var/www/fantry/uploads
 *      BASE URL: http://fantry-dev.duckdns.org/static/
 *
 * application.local.properties에서 경로 수정 가능합니다.
 * (맥 사용시 확인해주세요)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileManager fileManager;
    private final FileMetaRepository fileMetaRepository;

    @Transactional
    public List<FileMeta> uploadFiles(List<MultipartFile> files, String subDirectory, Member member) {
        if (files == null || files.isEmpty()) {
            throw new FileException(FileErrorCode.UPLOAD_FILE_EMPTY);
        }

        return files.stream()
                .map(file -> {
                    try {
                        return uploadFile(file, subDirectory, member);
                    } catch (FileException e) {
                        log.error("파일 업로드 중 오류 발생: {}", file.getOriginalFilename(), e);
                        return null; // 실패한 경우 null 반환
                    }
                })
                .filter(Objects::nonNull) // null이 아닌(성공한) 결과만 필터링
                .collect(Collectors.toList());
    }

    private FileMeta uploadFile(MultipartFile file, String subDirectory, Member member) throws FileException {
        if (file.isEmpty()) {
            throw new FileException(FileErrorCode.UPLOAD_FILE_EMPTY);
        }

        // 실제 서버(로컬)에 파일 저장
        String storedFilePath = fileManager.storeFile(file, subDirectory);
        String storedFileName = Paths.get(storedFilePath).getFileName().toString();

        // Filemeta 테이블에 저장
        FileMeta fileMeta = FileMeta.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedFileName)
                .storedFilePath(storedFilePath)
                .fileType(determineFileType(file.getContentType()))
                .fileSize((int) file.getSize())
                .fileExt(getFileExtension(file.getOriginalFilename()))
                .uploadedAt(LocalDateTime.now())
                .uploadedBy(member)
                .build();

        setImageDimensions(fileMeta, file);

        return fileMetaRepository.save(fileMeta);
    }

    private FileType determineFileType(String contentType) {
        if (contentType == null) {
            return FileType.ETC;
        }
        if (contentType.startsWith("image")) {
            return FileType.IMAGE;
        } else if (contentType.startsWith("video")) {
            return FileType.VIDEO;
        } else if (contentType.startsWith("audio")) {
            return FileType.AUDIO;
        } else if (contentType.startsWith("application") || contentType.startsWith("text")) {
            return FileType.DOCUMENT;
        } else {
            return FileType.ETC;
        }
    }

    @Transactional(readOnly = true)
    public Optional<FileMeta> getFileMeta(int fileMetaId){
        return fileMetaRepository.findById(fileMetaId);
    }

    @Transactional(readOnly = true)
    public String getFileAccessUrl(int fileMetaId){
        FileMeta fileMeta = fileMetaRepository.findById(fileMetaId)
                .orElseThrow(() -> new FileException(FileErrorCode.FILE_NOT_FOUND));
        return fileManager.getFileUrl(fileMeta.getStoredFilePath());
    }

    @Transactional
    public void deleteFile(int fileMetaId, Member member){
        FileMeta fileMeta = fileMetaRepository.findById(fileMetaId)
                .orElseThrow(() -> new FileException(FileErrorCode.FILE_NOT_FOUND));

        if (fileMeta.getUploadedBy() == null || !Objects.equals(fileMeta.getUploadedBy().getMemberId(), member.getMemberId())) {
            throw new FileException(FileErrorCode.ACCESS_DENIED);
        }

        fileMeta.setDeletedAt(LocalDateTime.now());

        String newPath = fileManager.moveToDeleted(fileMeta.getStoredFilePath());
        fileMeta.setStoredFilePath(newPath);
    }

    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }

    private void setImageDimensions(FileMeta fileMeta, MultipartFile file) {
        if (file.getContentType() != null && file.getContentType().startsWith("image")) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(file.getBytes())) {
                BufferedImage bimg = ImageIO.read(bis);
                if (bimg != null) {
                    fileMeta.setWidth(bimg.getWidth());
                    fileMeta.setHeight(bimg.getHeight());
                }
            } catch (IOException e) {
                log.warn("이미지 크기 추출 실패: " + file.getOriginalFilename(), e);
            }
        }
    }

}
