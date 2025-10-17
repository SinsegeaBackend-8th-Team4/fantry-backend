package com.eneifour.fantry.notice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notice_type")
public class NoticeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeTypeId;

    private String name;
}
