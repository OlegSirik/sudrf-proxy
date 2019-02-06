package com.vsk.courtrf.court.entity;

import javax.persistence.*;

@Entity
@Table(name="dict_courts_rf_terr")
public class CourtTerritory {

    @Id
    @SequenceGenerator(name="dict_courts_rf_seq",
            sequenceName="dict_courts_rf_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="dict_courts_rf_seq")
    private Long id;

    @Column(name="court_code")
    private String courtCode;
    private String col1;
    private String col2;
    private String indexCol;

    public CourtTerritory() {};

    public CourtTerritory(String pCode, String pCol1, String pCol2){
        this.col1 = pCol1.replace("'","");
        this.col2 = pCol2.replace("'","");
        this.indexCol = this.col1.toLowerCase() + " " + this.col2.toLowerCase();
        this.courtCode = pCode;
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

    public String getCourtCode() {
        return courtCode;
    }

    public void setCourtCode(String courtCode) {
        this.courtCode = courtCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndexCol() {
        return indexCol;
    }

    public void setIndexCol(String indexCol) {
        this.indexCol = indexCol;
    }

}
