package com.vsk.courtrf.lawcase.entity;

import javax.persistence.*;

@Entity
@Table(name="law_cases_search_req")
public class LawCaseSearchReq {

    @Id
    @SequenceGenerator(name="dict_courts_rf_seq",
            sequenceName="dict_courts_rf_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="dict_courts_rf_seq")
    private Integer Id;

    @Column(name="cases_source")
    private String caseSrc;
    @Column(name="search_word")
    private String searchWord;
    @Column(name="region_code")
    private String regionCode;
    @Column(name="days_back")
    private   int daysBack;

    public LawCaseSearchReq() {
    }

    public LawCaseSearchReq(String caseSrc, String searchWord, String regionCode, int daysBack) {
        this.caseSrc = caseSrc;
        this.searchWord = searchWord;
        this.regionCode = regionCode;
        this.daysBack = daysBack;
    }

    public String getCaseSrc() {
        return caseSrc;
    }

    public void setCaseSrc(String caseSrc) {
        this.caseSrc = caseSrc;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public int getDaysBack() {
        return daysBack;
    }

    public void setDaysBack(int daysBack) {
        this.daysBack = daysBack;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
