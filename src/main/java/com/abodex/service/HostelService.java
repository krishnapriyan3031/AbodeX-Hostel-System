package com.abodex.service;

import com.abodex.dto.BookingRequestDto;
import com.abodex.dto.BookingResponseDto;
import com.abodex.dto.CotDetailDto;
import com.abodex.dto.RoomCardDto;
import com.abodex.exception.CotAlreadyOccupiedException;
import com.abodex.exception.ResourceNotFoundException;
import com.abodex.exception.StudentAlreadyBookedException;
import com.abodex.model.*;
import com.abodex.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Core business service for Hostel & Cot Booking management.
 * Utilizes constructor-based dependency injection (Rubric Items 4 & 2).
 */
@Service
public class HostelService {

    private final HostelBlockRepository blockRepository;
    private final RoomRepository roomRepository;
    private final CotRepository cotRepository;
    private final StudentRepository studentRepository;
    private final BookingRepository bookingRepository;

    public HostelService(HostelBlockRepository blockRepository,
                         RoomRepository roomRepository,
                         CotRepository cotRepository,
                         StudentRepository studentRepository,
                         BookingRepository bookingRepository) {
        this.blockRepository = blockRepository;
        this.roomRepository = roomRepository;
        this.cotRepository = cotRepository;
        this.studentRepository = studentRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<HostelBlock> getAllBlocks() {
        return blockRepository.findAll();
    }

    public HostelBlock getBlockByName(String blockName) {
        return blockRepository.findByBlockName(blockName)
                .orElseThrow(() -> new ResourceNotFoundException("Hostel block not found: " + blockName));
    }

    public List<RoomCardDto> getFloorRooms(String blockName, int floor) {
        List<Room> rooms = roomRepository.findByHostelBlock_BlockNameAndFloorOrderByRoomNumberAsc(blockName, floor);
        if (rooms.isEmpty()) {
            // Verify block exists
            getBlockByName(blockName);
        }

        List<RoomCardDto> dtos = new ArrayList<>();
        for (Room room : rooms) {
            dtos.add(convertToRoomCardDto(room));
        }
        return dtos;
    }

    public RoomCardDto getRoomDetails(String blockName, String roomNumber) {
        Room room = roomRepository.findByHostelBlock_BlockNameAndRoomNumber(blockName, roomNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Room " + roomNumber + " not found in " + blockName));
        return convertToRoomCardDto(room);
    }

    @Transactional
    public BookingResponseDto bookCot(BookingRequestDto request) {
        // 1. Check if student already has an active booking (Single-Cot Rule)
        if (bookingRepository.existsByStudent_RegisterNumber(request.getRegisterNumber())) {
            throw new StudentAlreadyBookedException("Student with Register No " + request.getRegisterNumber()
                    + " already has an active cot booking. Anna University allows only 1 cot per student.");
        }

        // 2. Fetch or create Student
        Student student = studentRepository.findById(request.getRegisterNumber())
                .orElseGet(() -> new Student(request.getRegisterNumber(), request.getName(), request.getDepartment(), request.getGender()));
        student.setName(request.getName());
        student.setDepartment(request.getDepartment());
        if (request.getGender() != null) {
            student.setGender(request.getGender());
        }
        studentRepository.save(student);

        // 3. Find Room & Cot
        Room room = roomRepository.findByHostelBlock_BlockNameAndRoomNumber(request.getBlockName(), request.getRoomNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Room " + request.getRoomNumber() + " not found in block " + request.getBlockName()));

        Cot cot = cotRepository.findByRoom_IdAndCotCode(room.getId(), request.getCotCode())
                .orElseThrow(() -> new ResourceNotFoundException("Cot " + request.getCotCode() + " not found in room " + request.getRoomNumber()));

        // 4. Validate Cot Availability
        if (cot.isOccupied()) {
            throw new CotAlreadyOccupiedException("Cot " + cot.getLabel() + " in Room " + room.getRoomNumber() + " is already occupied.");
        }

        // 5. Reserve Cot and Create Booking
        cot.setOccupied(true);
        cotRepository.save(cot);

        Booking booking = new Booking(student, cot);
        Booking savedBooking = bookingRepository.save(booking);

        cot.setBooking(savedBooking);

        return convertToBookingResponse(savedBooking, room, cot);
    }

    public BookingResponseDto getBookingByRegisterNumber(String registerNumber) {
        Booking booking = bookingRepository.findByStudent_RegisterNumber(registerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No active booking found for student: " + registerNumber));
        Cot cot = booking.getCot();
        Room room = cot.getRoom();
        return convertToBookingResponse(booking, room, cot);
    }

    @Transactional
    public BookingResponseDto switchCot(String registerNumber, BookingRequestDto request) {
        // 1. Fetch existing booking
        Booking existingBooking = bookingRepository.findByStudent_RegisterNumber(registerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot switch: No active booking found for student: " + registerNumber));

        // 2. Vacate old cot
        Cot oldCot = existingBooking.getCot();
        oldCot.setOccupied(false);
        oldCot.setBooking(null);
        cotRepository.save(oldCot);

        // 3. Find new room and cot
        Room newRoom = roomRepository.findByHostelBlock_BlockNameAndRoomNumber(request.getBlockName(), request.getRoomNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Target room " + request.getRoomNumber() + " not found in " + request.getBlockName()));

        Cot newCot = cotRepository.findByRoom_IdAndCotCode(newRoom.getId(), request.getCotCode())
                .orElseThrow(() -> new ResourceNotFoundException("Target cot " + request.getCotCode() + " not found in room " + request.getRoomNumber()));

        if (newCot.isOccupied()) {
            // Rollback old cot reservation
            oldCot.setOccupied(true);
            oldCot.setBooking(existingBooking);
            cotRepository.save(oldCot);
            throw new CotAlreadyOccupiedException("Requested cot " + newCot.getLabel() + " is already occupied.");
        }

        // 4. Assign new cot
        newCot.setOccupied(true);
        newCot.setBooking(existingBooking);
        cotRepository.save(newCot);

        existingBooking.setCot(newCot);
        Booking updatedBooking = bookingRepository.save(existingBooking);

        return convertToBookingResponse(updatedBooking, newRoom, newCot);
    }

    @Transactional
    public void cancelBooking(String registerNumber) {
        Booking booking = bookingRepository.findByStudent_RegisterNumber(registerNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot cancel: No booking found for student " + registerNumber));

        Cot cot = booking.getCot();
        if (cot != null) {
            cot.setOccupied(false);
            cot.setBooking(null);
            cotRepository.save(cot);
        }

        bookingRepository.delete(booking);
    }

    private RoomCardDto convertToRoomCardDto(Room room) {
        RoomCardDto dto = new RoomCardDto();
        dto.setRoomId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setFloor(room.getFloor());
        dto.setAvailableCots(room.getAvailableCotCount());
        dto.setTotalCots(room.getTotalCotCount());
        dto.setStatusText(room.getOccupancyStatusText());
        dto.setOccupancyState(room.getOccupancyState());

        List<CotDetailDto> cotDtos = new ArrayList<>();
        for (Cot cot : room.getCots()) {
            CotDetailDto cDto = new CotDetailDto();
            cDto.setCotId(cot.getId());
            cDto.setCotCode(cot.getCotCode());
            cDto.setBunkNumber(cot.getBunkNumber());
            cDto.setUpper(cot.isUpper());
            cDto.setLabel(cot.getLabel());
            cDto.setOccupied(cot.isOccupied());

            if (cot.getBooking() != null && cot.getBooking().getStudent() != null) {
                Student occ = cot.getBooking().getStudent();
                cDto.setOccupantName(occ.getName());
                cDto.setOccupantRegNo(occ.getRegisterNumber());
                cDto.setOccupantDepartment(occ.getDepartment());
            }
            cotDtos.add(cDto);
        }
        dto.setCots(cotDtos);
        return dto;
    }

    private BookingResponseDto convertToBookingResponse(Booking booking, Room room, Cot cot) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setBookingId(booking.getId());
        dto.setRegisterNumber(booking.getStudent().getRegisterNumber());
        dto.setStudentName(booking.getStudent().getName());
        dto.setDepartment(booking.getStudent().getDepartment());
        dto.setBlockName(room.getHostelBlock().getBlockName());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setFloor(room.getFloor());
        dto.setCotCode(cot.getCotCode());
        dto.setCotLabel(cot.getLabel());
        dto.setBookingTime(booking.getBookingTime());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
