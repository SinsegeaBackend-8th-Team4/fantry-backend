package com.eneifour.fantry.common.util.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetaRepository extends JpaRepository<FileMeta, Integer> {
    Optional<FileMeta> findByStoredFilePath(String storedFilePath);
}