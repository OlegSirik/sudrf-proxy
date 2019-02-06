package com.vsk.courtrf.lawcase.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vsk.courtrf.util.Dt;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="law_case_calendar")
public class LawCaseCalendar {


    @Id
    @SequenceGenerator(name="dict_courts_rf_seq",
            sequenceName="dict_courts_rf_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="dict_courts_rf_seq")
    private Long id;

    @Column(name="case_id")
    private Long caseId;

    @Column(name="date_insert")
    @JsonFormat(pattern="dd.MM.yyyy")
    private Date dateInsert;
    @JsonFormat(pattern="dd.MM.yyyy")
    @Column(name="date_update")
    private Date dateUpdate;

    @Column(name="event_name")
    public String eventName;
    @Column(name="event_date")
    @JsonFormat(pattern="dd.MM.yyyy")
    public Date eventDate;
    @Column(name="event_time")
    public String eventTime;

    @Column(name="event_result")
    public String eventResult;
    @Column(name="event_reason")
    public String eventReason;

    private String lastError;

    public LawCaseCalendar() {
    }

    public LawCaseCalendar(Long caseId, String eventName, Date eventDate, String eventTime) {
        this.caseId = caseId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getEventDate() {
        return Dt.trunc(eventDate);
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = Dt.trunc(eventDate);
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventResult() {
        return eventResult;
    }

    public void setEventResult(String eventResult) {
        this.eventResult = eventResult;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }


    public String getEventReason() {
        return eventReason;
    }

    public void setEventReason(String eventReason) {
        this.eventReason = eventReason;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LawCaseCalendar that = (LawCaseCalendar) o;
        return
               Objects.equals(caseId, that.caseId) &&
               Objects.equals(eventName, that.eventName) &&
               Objects.equals( Dt.trunc(eventDate), Dt.trunc(that.eventDate)) &&
               Objects.equals(eventTime, that.eventTime) &&
               Objects.equals(eventResult, that.eventResult);
    }

    public String hashKey() {

        DateFormat outputFormatter = new SimpleDateFormat("dd.MM.yyyy");
        String dt = outputFormatter.format(eventDate);
        return eventName+"::"+dt+"::"+eventTime;
    }
}
