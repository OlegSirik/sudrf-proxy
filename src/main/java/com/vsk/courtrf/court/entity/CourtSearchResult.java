package com.vsk.courtrf.court.entity;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

public class CourtSearchResult {

    private Long id;
    private String code;
    private String address;
    private String phone;
    private String email;
    private String www;
    private String name;
    private List<String> addresses = new ArrayList();

    public CourtSearchResult(Long id, String code, String address, String phone, String email, String www, String name) {
        this.id = id;
        this.code = code;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.www = www;
        this.name = name;
    }

    public void addAddress(String address) {
        this.addresses.add(address);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }
}
