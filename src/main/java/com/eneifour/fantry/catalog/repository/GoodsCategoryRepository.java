package com.eneifour.fantry.catalog.repository;

import com.eneifour.fantry.catalog.domain.GoodsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsCategoryRepository extends JpaRepository<GoodsCategory, Integer> {

}
