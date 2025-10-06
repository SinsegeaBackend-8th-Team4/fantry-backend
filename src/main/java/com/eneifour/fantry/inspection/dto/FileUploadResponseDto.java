package com.eneifour.fantry.inspection.dto;

import com.eneifour.fantry.common.util.file.FileMeta;
import lombok.Builder;

@Builder
public record FileUploadResponseDto(
        int filemetaId,
        String originalFileName,
        String storedFileName,
        String fileUrl,
        long fileSize,
        String fileType,
        String fileExt
) {
    public static FileUploadResponseDto from(FileMeta fileMeta, String fileUrl) {
        return FileUploadResponseDto.builder()
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
