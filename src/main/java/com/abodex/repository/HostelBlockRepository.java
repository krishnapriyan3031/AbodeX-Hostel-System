package com.abodex.repository;

import com.abodex.model.HostelBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelBlockRepository extends JpaRepository<HostelBlock, Long> {
    Optional<HostelBlock> findByBlockName(String blockName);
}
