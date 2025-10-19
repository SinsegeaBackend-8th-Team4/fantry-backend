package com.eneifour.fantry.notice.repository;

import com.eneifour.fantry.notice.domain.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeTypeRepository extends JpaRepository<NoticeType, Integer> {
}
