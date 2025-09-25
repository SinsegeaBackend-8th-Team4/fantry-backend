package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.ChecklistTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate, Integer> {
}
