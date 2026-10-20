package com.abodex.repository;

import com.abodex.model.Cot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CotRepository extends JpaRepository<Cot, Long> {
    Optional<Cot> findByRoom_IdAndCotCode(Long roomId, String cotCode);
}
