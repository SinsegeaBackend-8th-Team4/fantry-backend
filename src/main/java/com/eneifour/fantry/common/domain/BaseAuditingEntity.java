package com.eneifour.fantry.common.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseAuditingEntity extends BaseTimeEntity { // BaseTimeEntity를 상속!

    @LastModifiedDate
    private LocalDateTime updatedAt;
}