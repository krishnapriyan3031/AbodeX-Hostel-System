package com.abodex.repository;

import com.abodex.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByStudent_RegisterNumber(String registerNumber);
    boolean existsByStudent_RegisterNumber(String registerNumber);
    boolean existsByCot_Id(Long cotId);
    void deleteByStudent_RegisterNumber(String registerNumber);
}
