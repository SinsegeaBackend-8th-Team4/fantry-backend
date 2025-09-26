package com.eneifour.fantry.common.util.file;


import com.eneifour.fantry.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="filemeta")
public class FileMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int filemetaId; // PK

    @Column(nullable = false)
    private String originalFileName; // 원본 파일 이름

    @Column(nullable = false, unique = true)
    private String storedFileName; // 저장된 파일 이름

    @Column(nullable = false)
    private String storedFilePath; // 저장된 경로

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private FileType fileType; // 파일 MIME 타입

    @Column(nullable = false)
    private int fileSize; // 파일 사이즈(크기)

    @Column(nullable = false)
    private String fileExt; // 파일 확장자

    @Column(updatable = false, nullable = false)
    private LocalDateTime uploadedAt; // 업로드된 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uploaded_by",referencedColumnName = "member_id", nullable = true)
    private Member uploadedBy; // FK 업로드한 멤버

    private LocalDateTime deletedAt; // 삭제된 날짜
    private Integer width; // (이미지일 경우) 사진 너비, 이미지가 아닌 경우 null
    private Integer height; // (이미지일 경우) 사진 높이, 이미지가 아닌 경우 null
}
