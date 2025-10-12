package com.eneifour.fantry.checklist.controller;

import com.eneifour.fantry.checklist.dto.OnlineChecklistResponse;
import com.eneifour.fantry.checklist.service.ChecklistService;
import com.eneifour.fantry.inspection.support.api.InspectionApiResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/checklists")
@RequiredArgsConstructor
public class ChecklistController {
    private final ChecklistService checklistService;

    // 카테고리별 체크리스트 목록 조회
    @GetMapping
    public InspectionApiResponse<OnlineChecklistResponse> getChecklistsByCategory(
            @RequestParam @NotNull @Positive int goodsCategoryId) {
        OnlineChecklistResponse checklistResponse = checklistService.getChecklist(goodsCategoryId);
        return InspectionApiResponse.ok(checklistResponse);
    }
}
