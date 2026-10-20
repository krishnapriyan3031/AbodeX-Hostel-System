package com.abodex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Entity representing an individual cot (Bunk 1 Upper, Bunk 1 Lower, Bunk 2 Upper, Bunk 2 Lower).
 */
@Entity
@Table(name = "cots")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cot_code", nullable = false, length = 20)
    private String cotCode; // "B1_U", "B1_L", "B2_U", "B2_L"

    @Column(name = "bunk_number", nullable = false)
    private int bunkNumber; // 1 or 2

    @Column(name = "is_upper", nullable = false)
    private boolean upper; // true = Upper cot, false = Lower cot

    @Column(name = "is_occupied", nullable = false)
    private boolean occupied;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    private Room room;

    @OneToOne(mappedBy = "cot", fetch = FetchType.EAGER)
    private Booking booking;

    public Cot() {}

    public Cot(String cotCode, int bunkNumber, boolean upper, Room room) {
        this.cotCode = cotCode;
        this.bunkNumber = bunkNumber;
        this.upper = upper;
        this.room = room;
        this.occupied = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCotCode() {
        return cotCode;
    }

    public void setCotCode(String cotCode) {
        this.cotCode = cotCode;
    }

    public int getBunkNumber() {
        return bunkNumber;
    }

    public void setBunkNumber(int bunkNumber) {
        this.bunkNumber = bunkNumber;
    }

    public boolean isUpper() {
        return upper;
    }

    public void setUpper(boolean upper) {
        this.upper = upper;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getLabel() {
        return "Bunk " + bunkNumber + " \u00B7 " + (upper ? "Upper" : "Lower");
    }
}
