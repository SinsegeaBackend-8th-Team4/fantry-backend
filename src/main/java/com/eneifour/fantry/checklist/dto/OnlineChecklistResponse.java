package com.eneifour.fantry.checklist.dto;

import com.eneifour.fantry.checklist.domain.ChecklistItem;
import com.eneifour.fantry.checklist.domain.ChecklistTemplate;

import java.util.List;

public record OnlineChecklistResponse(
        int templateId,
        int templateVersion,
        List<OnlineChecklistItemResponse> items
) {
    public static OnlineChecklistResponse from(ChecklistTemplate template, List<ChecklistItem> items) {
        return new OnlineChecklistResponse(
                template.getChecklistTemplateId(),
                template.getVersion(),
                items.stream().map(OnlineChecklistItemResponse::from).toList()
        );
    }
}
