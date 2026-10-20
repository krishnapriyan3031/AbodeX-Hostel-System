package com.abodex.controller;

import com.abodex.dto.RoomCardDto;
import com.abodex.model.HostelBlock;
import com.abodex.service.HostelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller exposing endpoints for Hostel Blocks, Floors, and Room availability.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Hostel Management", description = "Endpoints for exploring Anna University blocks, floors, and rooms")
public class HostelController {

    private final HostelService hostelService;

    public HostelController(HostelService hostelService) {
        this.hostelService = hostelService;
    }

    @Operation(summary = "Get all hostel blocks", description = "Returns the 13 Boys and 8 Girls hostel blocks")
    @GetMapping("/blocks")
    public ResponseEntity<List<HostelBlock>> getAllBlocks() {
        return ResponseEntity.ok(hostelService.getAllBlocks());
    }

    @Operation(summary = "Get floor rooms", description = "Returns 40 rooms for a specific floor with availability numbers")
    @GetMapping("/blocks/{blockName}/floors/{floor}")
    public ResponseEntity<List<RoomCardDto>> getFloorRooms(
            @PathVariable String blockName,
            @PathVariable int floor) {
        return ResponseEntity.ok(hostelService.getFloorRooms(blockName, floor));
    }

    @Operation(summary = "Get room cot details", description = "Returns individual cot occupancies (Bunk 1/2 Upper/Lower) for a room")
    @GetMapping("/blocks/{blockName}/rooms/{roomNumber}")
    public ResponseEntity<RoomCardDto> getRoomDetails(
            @PathVariable String blockName,
            @PathVariable String roomNumber) {
        return ResponseEntity.ok(hostelService.getRoomDetails(blockName, roomNumber));
    }
}
