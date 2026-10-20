package com.abodex.repository;

import com.abodex.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHostelBlock_BlockNameAndFloorOrderByRoomNumberAsc(String blockName, int floor);
    Optional<Room> findByHostelBlock_BlockNameAndRoomNumber(String blockName, String roomNumber);
}
