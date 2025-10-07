package com.eneifour.fantry.cs.repository;

import com.eneifour.fantry.cs.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FaqRepository extends JpaRepository<Faq, Integer>, JpaSpecificationExecutor<Faq> {

}
