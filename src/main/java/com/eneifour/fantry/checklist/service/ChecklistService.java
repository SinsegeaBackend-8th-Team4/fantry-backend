package com.eneifour.fantry.checklist.service;

import com.eneifour.fantry.checklist.dto.ChecklistItemDto;
import com.eneifour.fantry.checklist.repository.ChecklistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChecklistService {
    private final ChecklistItemRepository itemRepository;

    // 카테고리 별 체크리스트 항목 리스트
    public List<ChecklistItemDto> getItemsByCategory(int categoryId){
        return itemRepository.findByCategoryId(categoryId).stream()
                .map(ChecklistItemDto::from)
                .toList();
    }
}
