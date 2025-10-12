package com.eneifour.fantry.checklist.repository;

import com.eneifour.fantry.checklist.domain.ChecklistTemplate;
import com.eneifour.fantry.inspection.domain.ProductChecklistAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductChecklistAnswerRepository extends JpaRepository<ProductChecklistAnswer, ProductChecklistAnswer.Id> {
    List<ProductChecklistAnswer> findByProductInspection_ProductInspectionIdAndId_ChecklistRole(
            int productInspectionId,
            ChecklistTemplate.Role role
    );
}
