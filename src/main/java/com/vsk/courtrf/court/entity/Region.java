package com.vsk.courtrf.court.entity;


import javax.persistence.*;

@Entity
@Table(name="dict_regions")
public class Region {
    @Id
    @Column(name="id")
    private int id;

    @Column(name="code")
    private String code;

    @Column(name="name")
    private String name;

    public Region() {
    }

    public Region(String pCode, String pName) {
        id = Integer.parseInt(pCode);
        code = pCode;
        name = pName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
