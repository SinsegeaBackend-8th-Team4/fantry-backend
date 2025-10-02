package com.eneifour.fantry.inspection.dto;

import com.eneifour.fantry.common.util.file.FileMeta;
import lombok.Builder;

@Builder
public record FileUploadResponse(
        int filemetaId,
        String originalFileName,
        String storedFileName,
        String fileUrl,
        long fileSize,
        String fileType,
        String fileExt
) {
    public static FileUploadResponse from(FileMeta fileMeta, String fileUrl) {
        return FileUploadResponse.builder()
                .filemetaId(fileMeta.getFilemetaId())
                .originalFileName(fileMeta.getOriginalFileName())
                .storedFileName(fileMeta.getStoredFileName())
                .fileUrl(fileUrl)
                .fileSize(fileMeta.getFileSize())
                .fileType(fileMeta.getFileType().name())
                .fileExt(fileMeta.getFileExt())
                .build();
    }
}
