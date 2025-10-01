package com.eneifour.fantry.inspection.domain;

import com.eneifour.fantry.common.util.file.FileMeta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspection_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspectionFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inspectionFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_inspection_id")
    private ProductInspection productInspection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filemeta_id")
    private FileMeta fileMeta;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
