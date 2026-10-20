package com.abodex;

import com.abodex.dto.BookingRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/blocks should return 200 OK and list of blocks")
    void testGetAllBlocks() throws Exception {
        mockMvc.perform(get("/api/blocks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("GET /api/blocks/{block}/floors/0 should return 40 rooms")
    void testGetFloorRooms() throws Exception {
        mockMvc.perform(get("/api/blocks/Kurinji Block (Boys)/floors/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(40))
                .andExpect(jsonPath("$[0].roomNumber").value("001"));
    }

    @Test
    @DisplayName("POST /api/bookings should create allotment with 201 Created")
    void testCreateBookingEndpoint() throws Exception {
        BookingRequestDto req = new BookingRequestDto();
        req.setRegisterNumber("2023103555");
        req.setName("Web Tester");
        req.setDepartment("Computer Science");
        req.setBlockName("Kurinji Block (Boys)");
        req.setRoomNumber("008");
        req.setCotCode("B1_U");

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registerNumber").value("2023103555"))
                .andExpect(jsonPath("$.roomNumber").value("008"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @DisplayName("POST /api/bookings with invalid data should return 400 Bad Request with validation errors")
    void testValidationFailure() throws Exception {
        BookingRequestDto invalidReq = new BookingRequestDto();
        // Missing name, register number, block, room, etc.

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors").exists());
    }

    @Test
    @DisplayName("DELETE /api/bookings/{regNo} should return 204 No Content")
    void testDeleteBookingEndpoint() throws Exception {
        // First book
        BookingRequestDto req = new BookingRequestDto();
        req.setRegisterNumber("2023103444");
        req.setName("Delete Target");
        req.setDepartment("Civil");
        req.setBlockName("Kurinji Block (Boys)");
        req.setRoomNumber("011");
        req.setCotCode("B2_U");

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        // Then delete
        mockMvc.perform(delete("/api/bookings/2023103444"))
                .andExpect(status().isNoContent());
    }
}
