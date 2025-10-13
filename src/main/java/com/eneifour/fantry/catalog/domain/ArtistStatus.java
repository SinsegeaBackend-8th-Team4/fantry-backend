package com.eneifour.fantry.catalog.domain;

import lombok.Getter;

@Getter
public enum ArtistStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("반려");

    private final String label;

    ArtistStatus(String label) {this.label = label;}
}
