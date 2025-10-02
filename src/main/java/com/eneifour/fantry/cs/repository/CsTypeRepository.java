package com.eneifour.fantry.cs.repository;

import com.eneifour.fantry.cs.domain.CsType;
import com.eneifour.fantry.cs.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsTypeRepository extends JpaRepository<CsType, Integer>{
}