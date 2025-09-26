package com.eneifour.fantry.checklist.dto;

import com.eneifour.fantry.checklist.domain.ChecklistItem;

public record ChecklistItemDto(int checklistItemId, String itemKey, String label, String type, String options, boolean required, int orderIndex) {
    public static ChecklistItemDto from(ChecklistItem c) {
        return new ChecklistItemDto(
                c.getChecklistItemId(),
                c.getItemKey(),
                c.getLabel(),
                c.getType().name(),
                c.getOptions(),
                c.getRequired(),
                c.getOrderIndex()
        );
    }
}
