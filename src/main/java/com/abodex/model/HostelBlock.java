package com.abodex.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Hostel Block in Anna University (13 Boys Hostels, 8 Girls Hostels).
 */
@Entity
@Table(name = "hostel_blocks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HostelBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_name", nullable = false, unique = true, length = 100)
    private String blockName;

    @Column(name = "gender", nullable = false, length = 20)
    private String gender; // "Boys" or "Girls"

    @OneToMany(mappedBy = "hostelBlock", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Room> rooms = new ArrayList<>();

    public HostelBlock() {}

    public HostelBlock(String blockName, String gender) {
        this.blockName = blockName;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
