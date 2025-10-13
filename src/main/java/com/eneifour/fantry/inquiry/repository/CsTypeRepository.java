package com.eneifour.fantry.inquiry.repository;

import com.eneifour.fantry.inquiry.domain.CsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsTypeRepository extends JpaRepository<CsType, Integer>{
}