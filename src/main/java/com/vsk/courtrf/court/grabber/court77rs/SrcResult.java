package com.vsk.courtrf.court.grabber.court77rs;

public class SrcResult {
    private String house;
    private String court;

    public SrcResult(String house, String court) {
        this.house = house;
        this.court = court;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }
}
