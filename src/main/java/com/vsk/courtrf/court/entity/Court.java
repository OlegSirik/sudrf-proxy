package com.vsk.courtrf.court.entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="dict_courts_rf")
public class Court {
    public static String SUDRF = "RS";
    public static String SUDRFMIR = "MS";

    @Id
    @SequenceGenerator(name="dict_courts_rf_seq",
            sequenceName="dict_courts_rf_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="dict_courts_rf_seq")
    private Long id;

    @Column(name="court_okrug")
    private String okrug;
    @Column(name="court_kray")
    private String kray;
    @Column(name="court_code", unique=true)
    private String code;
    @Column(name="court_address")
    private String address;
    @Column(name="court_phone")
    private String phone;
    @Column(name="court_email")
    private String email;
    @Column(name="court_www")
    private String www;
    @Column(name="court_name")
    private String name;
    @Column(name="court_err")
    private String courtError;
    @Column(name="court_territory_err")
    private String courtTerrError;
    @Column(name="court_type")
    private String courtType;
    @Column(name="region_id")
    private int regionId;

    private Date lastUpdate;

    private String parentCourt;
    private String cityDistrict;
    private String courtDistrict;

    @Transient
    private List<CourtTerritory> court_territory = new ArrayList();

    public static String getSUDRF() {
        return SUDRF;
    }

    public static void setSUDRF(String SUDRF) {
        Court.SUDRF = SUDRF;
    }

    public static String getSUDRFMIR() {
        return SUDRFMIR;
    }

    public static void setSUDRFMIR(String SUDRFMIR) {
        Court.SUDRFMIR = SUDRFMIR;
    }

    public String getOkrug() {
        return okrug;
    }

    public void setOkrug(String okrug) {
        this.okrug = okrug;
    }

    public String getKray() {
        return kray;
    }

    public void setKray(String kray) {
        this.kray = kray;
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

    public String getCourtError() {
        return courtError;
    }

    public void setCourtError(String court_err) {
        this.courtError = court_err;
    }

    public String getCourtTerrError() {
        return courtTerrError;
    }

    public void setCourtTerrError(String court_territory_err) {
        this.courtTerrError = court_territory_err;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(String court_type) {
        this.courtType = court_type;
    }

    public List<CourtTerritory> getCourt_territory() {
        return court_territory;
    }

    public void setCourt_territory(List<CourtTerritory> court_territory) { this.court_territory = court_territory; };

    public void addTerritory(String col1, String col2) {
        this.getCourt_territory().add( new CourtTerritory( this.code, col1, col2));
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentCourt() {
        return parentCourt;
    }

    public void setParentCourt(String parentCourt) {
        this.parentCourt = parentCourt;
    }

    public String getCityDistrict() {
        return cityDistrict;
    }

    public void setCityDistrict(String cityDistrict) {
        this.cityDistrict = cityDistrict;
    }

    public String getCourtDistrict() {
        return courtDistrict;
    }

    public void setCourtDistrict(String courtDistrict) {
        this.courtDistrict = courtDistrict;
    }

    @Override
    public String toString() {
        return code + "::" + okrug + "::" + kray + "::" + address
                + "::" + phone  + "::" + email + "::" + www + "::" + name + "::" + regionId;
    }
}
