package com.eneifour.fantry.inspection.repository;

import com.eneifour.fantry.inspection.domain.InspectionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionFileRepository extends JpaRepository<InspectionFile, Integer> {
}
