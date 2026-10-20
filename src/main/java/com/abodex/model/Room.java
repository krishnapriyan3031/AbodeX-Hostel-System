package com.abodex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a hostel room containing 4 cots (2 bunk beds).
 */
@Entity
@Table(name = "rooms")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, length = 10)
    private String roomNumber; // e.g. "001", "014", "101", etc.

    @Column(name = "floor", nullable = false)
    private int floor; // 0 = Ground, 1 = 1st, 2 = 2nd, 3 = 3rd

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id", nullable = false)
    private HostelBlock hostelBlock;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Cot> cots = new ArrayList<>();

    public Room() {}

    public Room(String roomNumber, int floor, HostelBlock hostelBlock) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.hostelBlock = hostelBlock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public HostelBlock getHostelBlock() {
        return hostelBlock;
    }

    public void setHostelBlock(HostelBlock hostelBlock) {
        this.hostelBlock = hostelBlock;
    }

    public List<Cot> getCots() {
        return cots;
    }

    public void setCots(List<Cot> cots) {
        this.cots = cots;
    }

    public int getAvailableCotCount() {
        int count = 0;
        for (Cot cot : cots) {
            if (!cot.isOccupied()) {
                count++;
            }
        }
        return count;
    }

    public int getTotalCotCount() {
        return cots.size();
    }

    public String getOccupancyStatusText() {
        return getAvailableCotCount() + "/" + getTotalCotCount() + " free";
    }

    public String getOccupancyState() {
        int free = getAvailableCotCount();
        if (free == getTotalCotCount()) return "AVAILABLE";
        if (free == 0) return "FULLY_OCCUPIED";
        return "PARTIALLY_OCCUPIED";
    }
}
