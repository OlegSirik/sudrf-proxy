package com.vsk.courtrf.lawcase.controller.request;

public class AddLawCase {
    private String href;

    public AddLawCase(String href) {
        this.href = href;
    }

    public AddLawCase() {
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
