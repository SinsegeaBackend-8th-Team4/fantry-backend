package com.eneifour.fantry.catalog.domain;

import lombok.Getter;

@Getter
public enum GroupType {
    MALE_GROUP("남자 그룹"), 
    FEMALE_GROUP("여자 그룹"),
    MALE_SOLO("남자 솔로"),
    FEMALE_SOLO("여자 솔로"),
    MIXED("혼성 그룹"),
    OTHER("기타");

    private final String label;

    GroupType(String label) {this.label = label;}
}
