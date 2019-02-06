package com.vsk.courtrf.lawcase.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="law_case_details")
public class LawCaseDetails {

    @Id
    @SequenceGenerator(name="dict_courts_rf_seq",
            sequenceName="dict_courts_rf_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="dict_courts_rf_seq")
    private Long id;

    @Column(name="case_id")
    private Long caseId;

    @Column(name="pageNr")
    public int pageNr;
    @Column(name="rowNr")
    public int rowNr;
    @Column(name="col1")
    public String col1;
    @Column(name="col2")
    public String col2;
    @Column(name="col3")
    public String col3;
    @Column(name="col4")
    public String col4;
    @Column(name="col5")
    public String col5;
    @Column(name="col6")
    public String col6;

    @Column(name="hash_Code")
    private int hashCode;

    public LawCaseDetails() {
    }

    public LawCaseDetails( Long pCaseId, int pPageNr,int pRowNr,String col1,String col2,String col3,String col4,String col5,String col6 ) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        this.col5 = col5;
        this.col6 = col6;
        this.caseId = pCaseId;
        this.pageNr = pPageNr;
        this.rowNr = pRowNr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCaseId() {
        return caseId;
    }

    public void setCaseId(long caseId) {
        this.caseId = caseId;
    }

    public int getPageNr() {
        return pageNr;
    }

    public void setPageNr(int pageNr) {
        this.pageNr = pageNr;
    }

    public int getRowNr() {
        return rowNr;
    }

    public void setRowNr(int rowNr) {
        this.rowNr = rowNr;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public String getCol5() {
        return col5;
    }

    public void setCol5(String col5) {
        this.col5 = col5;
    }

    public String getCol6() {
        return col6;
    }

    public void setCol6(String col6) {
        this.col6 = col6;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode() {
        this.hashCode = hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash( caseId, pageNr, rowNr, col1, col2, col3, col4, col5, col6);
    }
}
