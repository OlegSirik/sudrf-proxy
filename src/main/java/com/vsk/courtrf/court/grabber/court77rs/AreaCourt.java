package com.vsk.courtrf.court.grabber.court77rs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaCourt {
    private Court court;
    private String areavnpid;

    public AreaCourt() {
    }


    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public String getAreavnpid() {
        return areavnpid;
    }

    public void setAreavnpid(String areavnpid) {
        this.areavnpid = areavnpid;
    }
}
