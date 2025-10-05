package com.eneifour.fantry.inspection.repository;

import com.eneifour.fantry.inspection.domain.ProductInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionRepository extends JpaRepository<ProductInspection, Integer> {
}
