package com.eneifour.fantry.checklist.controller;

import com.eneifour.fantry.checklist.dto.ChecklistItemDto;
import com.eneifour.fantry.checklist.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/checklists")
@RequiredArgsConstructor
public class ChecklistController {
    private final ChecklistService checklistService;

    @GetMapping
    public List<ChecklistItemDto> getChecklistByCategory(@RequestParam int goodsCategoryId) {
        return checklistService.getItemsByCategory(goodsCategoryId);
    }
}
