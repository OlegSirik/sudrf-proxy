package com.vsk.courtrf.lawcase.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="law_cases")
public class LawCase {

    public static String UPDATE_MAKE = "UPDATE";
    public static String UPDATE_STOP = "STOP";

    public static String SRC_SUDRF = "SUDRF";
    public static String SRC_MOSSUD = "MOSSUD";
    public static String SRC_ARBITR = "ARBITR";

    @Id
    @SequenceGenerator(name="dict_courts_rf_seq",
            sequenceName="dict_courts_rf_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="dict_courts_rf_seq")
    private Long id;

    @Column(name="case_region")
    private String caseRegion;
    @Column(name="case_court")
    private String caseCourt;
    @Column(name="case_Nr")
    private String caseNr;
    @Column(name="case_Href")
    private String caseHref;
    @Column(name="case_Dt")
    private String caseDt;
    @Column(name="case_Info")
    private String caseInfo;
    @Column(name="case_Judge")
    private String caseJudge;
    @Column(name="case_Result")
    private String caseResult;

    @Column(name="case_side1", length=4000)
    private String caseSide1;
    @Column(name="case_side2", length=4000)
    private String caseSide2;
    @Column(name="case_category", length=4000)
    private String caseCategory;
    @Column(name="case_status", length=4000)
    private String caseStatus;
    @Column(name="case_code", length=4000)
    private String caseCode;


    @Column(name="court_Act")
    private String courtAct;
    @Column(name="court_ActHref")
    private String courtActHref;
    @Column(name="update_Flag")
    private String updateFlag;
    @Column(name="last_Error")
    private String lastError;

    @Column(name="date_insert")
    @JsonFormat(pattern="dd.MM.yyyy")
    private Date dateInsert;
    @Column(name="date_Update")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd.MM.yyyy")
    private Date dateUpdate;
    @Column(name="date_sync")
    @JsonFormat(pattern="dd.MM.yyyy")
    private Date dateSync;

    @Column(name="case_src")
    private String caseSrc;


    @Transient
    private List<LawCaseCalendar> calendar = new ArrayList();
    @Transient
    private List<LawCaseDetails> details = new ArrayList();

    public String getCaseRegion() {
        return caseRegion;
    }

    public void setCaseRegion(String caseRegion) {
        this.caseRegion = caseRegion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseCourt() {
        return caseCourt;
    }

    public void setCaseCourt(String caseCourt) {
        this.caseCourt = caseCourt;
    }

    public String getCaseNr() {
        return caseNr;
    }

    public void setCaseNr(String caseNr) {
        this.caseNr = caseNr;
    }

    public String getCaseHref() {
        return caseHref;
    }

    public void setCaseHref(String caseHref) {
        this.caseHref = caseHref;
    }

    public String getCaseDt() {
        return caseDt;
    }

    public void setCaseDt(String caseDt) {
        this.caseDt = caseDt;
    }

    public String getCaseInfo() {
        return caseInfo;
    }

    public void setCaseInfo(String caseInfo) {
        this.caseInfo = caseInfo;
    }

    public String getCaseJudge() {
        return caseJudge;
    }

    public void setCaseJudge(String caseJudge) {
        this.caseJudge = caseJudge;
    }

    public String getCaseResult() {
        return caseResult;
    }

    public void setCaseResult(String caseResult) {
        this.caseResult = caseResult;
    }

    public String getCourtAct() {
        return courtAct;
    }

    public void setCourtAct(String courtAct) {
        this.courtAct = courtAct;
    }

    public String getCourtActHref() {
        return courtActHref;
    }

    public void setCourtActHref(String courtActHref) {
        this.courtActHref = courtActHref;
    }


    public void addDetail(int pPageNr, int pRowNr, String p1,String p2,String p3,String p4,String p5,String p6 )
    {
        this.details.add( new LawCaseDetails( this.id, pPageNr, pRowNr,p1,p2,p3,p4,p5,p6 ) );
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public List<LawCaseDetails> getDetails() {
        return this.details;
    }

    public void setDetails(List<LawCaseDetails> details) {
        this.details = details;
    }

    public List<LawCaseCalendar> getCalendar() {
        return calendar;
    }

    public void setCalendar( List<LawCaseCalendar> calendar) {
        this.calendar = calendar;
    }

    public String getCaseSide1() {
        return caseSide1;
    }

    public void setCaseSide1(String caseSide1) {
        this.caseSide1 = caseSide1;
    }

    public String getCaseSide2() {
        return caseSide2;
    }

    public void setCaseSide2(String caseSide2) {
        this.caseSide2 = caseSide2;
    }

    public String getCaseCategory() {
        return caseCategory;
    }

    public void setCaseCategory(String caseCategory) {
        this.caseCategory = caseCategory;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public String getCaseSrc() {
        return caseSrc;
    }

    public void setCaseSrc(String  caseSrc) {
        this.caseSrc = caseSrc;
    }

    public Date getDateInsert() {
        return dateInsert;
    }

    public void setDateInsert(Date dateInsert) {
        this.dateInsert = dateInsert;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSync() {
        return dateSync;
    }

    public void setDateSync(Date dateSync) {
        this.dateSync = dateSync;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LawCase lawCase = (LawCase) o;
        return hashCode() == lawCase.hashCode() &&
                Objects.equals(caseRegion, lawCase.caseRegion) &&
                Objects.equals(caseCourt, lawCase.caseCourt) &&
                Objects.equals(caseNr, lawCase.caseNr) &&
                Objects.equals(caseHref, lawCase.caseHref) &&
                Objects.equals(caseDt, lawCase.caseDt) &&
                Objects.equals(caseInfo, lawCase.caseInfo) &&
                Objects.equals(caseJudge, lawCase.caseJudge) &&
                Objects.equals(caseResult, lawCase.caseResult) &&
                Objects.equals(courtAct, lawCase.courtAct) &&
                Objects.equals(courtActHref, lawCase.courtActHref);
    }

    @Override
    // регион суд и номер должны быть уникальными
    public int hashCode() {
        return Objects.hash( caseRegion, caseCourt, caseNr );
    }
}
