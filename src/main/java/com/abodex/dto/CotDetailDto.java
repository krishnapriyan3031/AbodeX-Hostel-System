package com.abodex.dto;

/**
 * DTO for cot details displayed in the cot modal.
 */
public class CotDetailDto {
    private Long cotId;
    private String cotCode;
    private int bunkNumber;
    private boolean upper;
    private String label;
    private boolean occupied;
    private String occupantName;
    private String occupantRegNo;
    private String occupantDepartment;

    public CotDetailDto() {}

    public Long getCotId() { return cotId; }
    public void setCotId(Long cotId) { this.cotId = cotId; }

    public String getCotCode() { return cotCode; }
    public void setCotCode(String cotCode) { this.cotCode = cotCode; }

    public int getBunkNumber() { return bunkNumber; }
    public void setBunkNumber(int bunkNumber) { this.bunkNumber = bunkNumber; }

    public boolean isUpper() { return upper; }
    public void setUpper(boolean upper) { this.upper = upper; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public String getOccupantName() { return occupantName; }
    public void setOccupantName(String occupantName) { this.occupantName = occupantName; }

    public String getOccupantRegNo() { return occupantRegNo; }
    public void setOccupantRegNo(String occupantRegNo) { this.occupantRegNo = occupantRegNo; }

    public String getOccupantDepartment() { return occupantDepartment; }
    public void setOccupantDepartment(String occupantDepartment) { this.occupantDepartment = occupantDepartment; }
}
