package com.eneifour.fantry.common.util.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/***
 * 시스템(서버 or 로컬)에 물리적 파일 저장
 * @author 재환
 */
@Component
@Slf4j
public class FileManager {

    private static final String DELETED_SUBDIRECTORY = "deleted";

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;

    public String storeFile(MultipartFile file, String subDirectory) {
        if (file.isEmpty()) {
            throw new FileException(FileErrorCode.UPLOAD_FILE_EMPTY);
        }

        try {
            Path uploadPath = Paths.get(uploadDir, subDirectory).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedFileName = UUID.randomUUID().toString() + fileExtension;
            Path targetLocation = uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), targetLocation);

            log.debug("파일 저장 경로 : {}", targetLocation);
            return Paths.get(subDirectory, storedFileName).toString().replace('\\', '/');
        } catch (IOException e) {
            throw new FileException(FileErrorCode.FILE_STORAGE_FAILED, e);
        }
    }

    public String moveToDeleted(String storedFilePath) {
        try {
            Path originalRelativePath = Paths.get(storedFilePath);
            Path parentDirectory = originalRelativePath.getParent();
            String filename = originalRelativePath.getFileName().toString();

            Path newRelativePath = (parentDirectory != null)
                    ? parentDirectory.resolve(DELETED_SUBDIRECTORY).resolve(filename)
                    : Paths.get(DELETED_SUBDIRECTORY, filename);

            Path sourcePath = Paths.get(uploadDir, storedFilePath).toAbsolutePath().normalize();
            if (!Files.exists(sourcePath)) {
                log.warn("삭제할 파일이 존재하지 않습니다: {}", sourcePath);
                return newRelativePath.toString().replace('\\', '/');
            }

            Path targetPath = Paths.get(uploadDir).resolve(newRelativePath).toAbsolutePath().normalize();
            Files.createDirectories(targetPath.getParent());

            try {
                Files.move(sourcePath, targetPath);
                log.info("파일을 삭제 폴더로 이동했습니다: {} -> {}", sourcePath, targetPath);
            } catch (FileAlreadyExistsException e) {
                log.warn("파일이 이미 삭제 폴더에 존재합니다: {}. 원본 파일을 삭제합니다.", targetPath);
                deleteFilePermanently(sourcePath.toString());
            }

            return newRelativePath.toString().replace('\\', '/');
        } catch (IOException e) {
            throw new FileException(FileErrorCode.FILE_DELETE_FAILED, e);
        }
    }

    public String restoreFile(String deletedFilePath) {
        try {
            Path deletedRelativePath = Paths.get(deletedFilePath);
            Path deletedFolder = deletedRelativePath.getParent(); // .../test/upload/deleted
            String filename = deletedRelativePath.getFileName().toString();

            if (deletedFolder == null || !deletedFolder.endsWith(DELETED_SUBDIRECTORY)) {
                throw new FileException(FileErrorCode.FILE_RESTORE_FAILED, new IllegalArgumentException("잘못된 삭제 경로 형식입니다."));
            }

            Path originalParentDirectory = deletedFolder.getParent(); // .../test/upload
            Path originalRelativePath = (originalParentDirectory != null)
                    ? originalParentDirectory.resolve(filename)
                    : Paths.get(filename);

            Path sourcePath = Paths.get(uploadDir, deletedFilePath).toAbsolutePath().normalize();
            if (!Files.exists(sourcePath)) {
                throw new FileException(FileErrorCode.FILE_NOT_FOUND);
            }

            Path targetPath = Paths.get(uploadDir).resolve(originalRelativePath).toAbsolutePath().normalize();
            Files.createDirectories(targetPath.getParent());
            Files.move(sourcePath, targetPath);
            log.info("파일을 복구했습니다: {} -> {}", sourcePath, targetPath);

            return originalRelativePath.toString().replace('\\', '/');
        } catch (IOException e) {
            throw new FileException(FileErrorCode.FILE_RESTORE_FAILED, e);
        }
    }

    public boolean deleteFilePermanently(String storedFilePath) {
        Path filePath = Paths.get(uploadDir, storedFilePath).toAbsolutePath().normalize();
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("파일을 영구 삭제하는 중 오류가 발생했습니다: {}", filePath, e);
            return false;
        }
    }

    public String getFileUrl(String storedFilePath) {
        if (storedFilePath == null || storedFilePath.isBlank()) {
            return "";
        }
        // 삭제된 파일에 대한 접근 제어 로직
        if (storedFilePath.contains("/" + DELETED_SUBDIRECTORY + "/")) {
            log.warn("삭제된 파일에 대한 접근이 시도되었습니다: {}", storedFilePath);
        }
        log.debug("파일 접근 URL : {}", (baseUrl + storedFilePath));
        return baseUrl + storedFilePath;
    }
}
