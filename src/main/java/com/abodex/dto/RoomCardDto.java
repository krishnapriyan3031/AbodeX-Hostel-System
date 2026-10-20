package com.abodex.dto;

import java.util.List;

/**
 * DTO for displaying room cards and cot status.
 */
public class RoomCardDto {
    private Long roomId;
    private String roomNumber;
    private int floor;
    private int availableCots;
    private int totalCots;
    private String statusText;
    private String occupancyState; // "AVAILABLE", "PARTIALLY_OCCUPIED", "FULLY_OCCUPIED"
    private List<CotDetailDto> cots;

    public RoomCardDto() {}

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    public int getAvailableCots() { return availableCots; }
    public void setAvailableCots(int availableCots) { this.availableCots = availableCots; }

    public int getTotalCots() { return totalCots; }
    public void setTotalCots(int totalCots) { this.totalCots = totalCots; }

    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }

    public String getOccupancyState() { return occupancyState; }
    public void setOccupancyState(String occupancyState) { this.occupancyState = occupancyState; }

    public List<CotDetailDto> getCots() { return cots; }
    public void setCots(List<CotDetailDto> cots) { this.cots = cots; }
}
