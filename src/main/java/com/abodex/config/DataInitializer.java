package com.abodex.config;

import com.abodex.model.*;
import com.abodex.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Initializes the Anna University hostel structure:
 * 13 Boys Hostels, 8 Girls Hostels, 4 Floors, 40 Rooms per floor, 4 Cots per room (640 cots).
 * Pre-populates realistic data matching the user's reference UI.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final HostelBlockRepository blockRepository;
    private final RoomRepository roomRepository;
    private final CotRepository cotRepository;
    private final StudentRepository studentRepository;
    private final BookingRepository bookingRepository;

    public DataInitializer(HostelBlockRepository blockRepository,
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

    public static final String[] BOYS_HOSTELS = {
        "Kurinji Block (Boys)", "Mullai Block (Boys)", "Marutham Block (Boys)",
        "Kaveri Block (Boys)", "Vaigai Block (Boys)", "Thamirabarani Block (Boys)",
        "Bhavani Block (Boys)", "Amaravathi Block (Boys)", "Ponni Block (Boys)",
        "Palar Block (Boys)", "Cheyyar Block (Boys)", "Manimuthar Block (Boys)",
        "Siruvani Block (Boys)"
    };

    public static final String[] GIRLS_HOSTELS = {
        "Ganga Block (Girls)", "Yamuna Block (Girls)", "Godavari Block (Girls)",
        "Narmada Block (Girls)", "Krishna Block (Girls)", "Saraswathi Block (Girls)",
        "Thamarai Block (Girls)", "Malligai Block (Girls)"
    };

    @Override
    public void run(String... args) {
        if (blockRepository.count() > 0) return;

        // 1. Create all 13 boys hostels
        List<HostelBlock> blocks = new ArrayList<>();
        for (String name : BOYS_HOSTELS) {
            blocks.add(new HostelBlock(name, "Boys"));
        }
        for (String name : GIRLS_HOSTELS) {
            blocks.add(new HostelBlock(name, "Girls"));
        }
        blockRepository.saveAll(blocks);

        // 2. Generate full 160 rooms (40 per floor) for Kurinji Block (Boys)
        HostelBlock kurinji = blockRepository.findByBlockName("Kurinji Block (Boys)").orElse(null);
        if (kurinji != null) {
            initializeBlockRooms(kurinji);
            createSampleBookings(kurinji);
        }

        // 3. Generate rooms for Ganga Block (Girls) as well
        HostelBlock ganga = blockRepository.findByBlockName("Ganga Block (Girls)").orElse(null);
        if (ganga != null) {
            initializeBlockRooms(ganga);
        }
    }

    private void initializeBlockRooms(HostelBlock block) {
        List<Room> rooms = new ArrayList<>();
        for (int floor = 0; floor < 4; floor++) {
            for (int r = 1; r <= 40; r++) {
                String roomNumber = (floor == 0) ? String.format("%03d", r) : String.format("%d", (floor * 100) + r);
                Room room = new Room(roomNumber, floor, block);
                rooms.add(room);
            }
        }
        List<Room> savedRooms = roomRepository.saveAll(rooms);

        // For each room, add 4 cots (B1_U, B1_L, B2_U, B2_L)
        List<Cot> cots = new ArrayList<>();
        for (Room r : savedRooms) {
            cots.add(new Cot("B1_U", 1, true, r));
            cots.add(new Cot("B1_L", 1, false, r));
            cots.add(new Cot("B2_U", 2, true, r));
            cots.add(new Cot("B2_L", 2, false, r));
        }
        cotRepository.saveAll(cots);
    }

    private void createSampleBookings(HostelBlock block) {
        // Room 002: 2 booked (2/4 free)
        addSampleBooking(block, "002", "B1_U", "202310101", "Karthik R", "Computer Science and Engineering");
        addSampleBooking(block, "002", "B2_L", "202310102", "Dinesh K", "Electrical & Electronics");

        // Room 003: 4 booked (0/4 free - fully occupied)
        addSampleBooking(block, "003", "B1_U", "202310103", "Manoj P", "Mechanical Engineering");
        addSampleBooking(block, "003", "B1_L", "202310104", "Vignesh S", "Civil Engineering");
        addSampleBooking(block, "003", "B2_U", "202310105", "Sanjay M", "Electronics & Communication");
        addSampleBooking(block, "003", "B2_L", "202310106", "Aravind T", "Information Technology");

        // Room 004: 1 booked (3/4 free)
        addSampleBooking(block, "004", "B1_L", "202310107", "Rohan V", "Biotechnology");

        // Room 006: 3 booked (1/4 free)
        addSampleBooking(block, "006", "B1_U", "202310108", "Deepak N", "Computer Science");
        addSampleBooking(block, "006", "B1_L", "202310109", "Praveen B", "Computer Science");
        addSampleBooking(block, "006", "B2_L", "202310110", "Harish G", "Information Technology");

        // Room 014: exactly matching reference image 3!
        // Student A on Bunk 1 Lower
        addSampleBooking(block, "014", "B1_L", "202310122", "Student A", "Computer Science");
    }

    private void addSampleBooking(HostelBlock block, String roomNo, String cotCode, String regNo, String name, String dept) {
        Student student = new Student(regNo, name, dept, block.getGender());
        studentRepository.save(student);

        Room room = roomRepository.findByHostelBlock_BlockNameAndRoomNumber(block.getBlockName(), roomNo).orElse(null);
        if (room != null) {
            Cot cot = cotRepository.findByRoom_IdAndCotCode(room.getId(), cotCode).orElse(null);
            if (cot != null) {
                cot.setOccupied(true);
                cotRepository.save(cot);

                Booking booking = new Booking(student, cot);
                Booking savedBooking = bookingRepository.save(booking);
                cot.setBooking(savedBooking);
            }
        }
    }
}
