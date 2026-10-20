package com.abodex;

import com.abodex.dto.BookingRequestDto;
import com.abodex.dto.BookingResponseDto;
import com.abodex.exception.CotAlreadyOccupiedException;
import com.abodex.exception.StudentAlreadyBookedException;
import com.abodex.service.HostelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingServiceTest {

    @Autowired
    private HostelService hostelService;

    @Test
    @DisplayName("Test 1: Successfully book an available cot")
    void testSuccessfulCotBooking() {
        BookingRequestDto req = new BookingRequestDto();
        req.setRegisterNumber("2023103999");
        req.setName("Test Student");
        req.setDepartment("Computer Science");
        req.setBlockName("Kurinji Block (Boys)");
        req.setRoomNumber("001");
        req.setCotCode("B1_U");

        BookingResponseDto response = hostelService.bookCot(req);

        assertNotNull(response);
        assertEquals("2023103999", response.getRegisterNumber());
        assertEquals("001", response.getRoomNumber());
        assertEquals("CONFIRMED", response.getStatus());
    }

    @Test
    @DisplayName("Test 2: Reject booking if student already holds a cot (Single-Cot Rule)")
    void testPreventDuplicateBooking() {
        BookingRequestDto req1 = new BookingRequestDto();
        req1.setRegisterNumber("2023103888");
        req1.setName("Duplicate Tester");
        req1.setDepartment("IT");
        req1.setBlockName("Kurinji Block (Boys)");
        req1.setRoomNumber("001");
        req1.setCotCode("B1_U");
        hostelService.bookCot(req1);

        // Try booking a second cot with the same register number
        BookingRequestDto req2 = new BookingRequestDto();
        req2.setRegisterNumber("2023103888");
        req2.setName("Duplicate Tester");
        req2.setDepartment("IT");
        req2.setBlockName("Kurinji Block (Boys)");
        req2.setRoomNumber("001");
        req2.setCotCode("B1_L");

        assertThrows(StudentAlreadyBookedException.class, () -> hostelService.bookCot(req2));
    }

    @Test
    @DisplayName("Test 3: Reject booking if cot is already occupied")
    void testRejectOccupiedCot() {
        BookingRequestDto req = new BookingRequestDto();
        req.setRegisterNumber("2023103777");
        req.setName("Conflict Student");
        req.setDepartment("ECE");
        req.setBlockName("Kurinji Block (Boys)");
        req.setRoomNumber("014"); // Pre-populated in DataInitializer with Student A
        req.setCotCode("B1_L");

        assertThrows(CotAlreadyOccupiedException.class, () -> hostelService.bookCot(req));
    }

    @Test
    @DisplayName("Test 4: Cancel and vacate booking")
    void testCancelBooking() {
        BookingRequestDto req = new BookingRequestDto();
        req.setRegisterNumber("2023103666");
        req.setName("Vacate Tester");
        req.setDepartment("Mechanical");
        req.setBlockName("Kurinji Block (Boys)");
        req.setRoomNumber("005");
        req.setCotCode("B2_U");
        hostelService.bookCot(req);

        // Cancel
        assertDoesNotThrow(() -> hostelService.cancelBooking("2023103666"));

        // Verify cot can now be booked again
        BookingResponseDto rebooked = hostelService.bookCot(req);
        assertNotNull(rebooked);
    }
}
