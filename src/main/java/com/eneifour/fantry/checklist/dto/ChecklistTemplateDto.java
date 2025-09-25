package com.eneifour.fantry.checklist.dto;

import com.eneifour.fantry.checklist.domain.ChecklistTemplate;

public record ChecklistTemplateDto(int checklistTemplateId, String code, String title, String role, String status) {
    public static ChecklistTemplateDto from(ChecklistTemplate t) {
        return new ChecklistTemplateDto(
                t.getChecklistTemplateId(),
                t.getCode(),
                t.getTitle(),
                t.getRole().name(),
                t.getStatus().name()
        );
    }
}
