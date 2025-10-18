package com.eneifour.fantry.dashboard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberStats {
    private final long totalMembers;
    private final long todayRegisteredMembers;
}