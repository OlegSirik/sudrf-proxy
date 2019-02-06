package com.vsk.courtrf.court.grabber.court77rs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resp {
    private String id;
    private String display_name;
    private List<StreetDetails> children;

    public Resp() {
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

    public List<StreetDetails> getChildren() {
        return children;
    }

    public void setChildren(List<StreetDetails> children) {
        this.children = children;
    }
}
