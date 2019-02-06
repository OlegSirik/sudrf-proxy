package com.vsk.courtrf.court.grabber.court77rs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StreetDetails {
    private String id;
    private String display_name;
    private List<AreaCourt> area_courts;

    public StreetDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public List<AreaCourt> getArea_courts() {
        return area_courts;
    }

    public void setArea_courts(List<AreaCourt> area_courts) {
        this.area_courts = area_courts;
    }
}
