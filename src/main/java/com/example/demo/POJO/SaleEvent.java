package com.example.demo.POJO;

import java.time.LocalDate;
import java.util.Date;

public class SaleEvent {
    private String EventType;
    private int id;
    private LocalDate date;


    public SaleEvent(){
    }
    public SaleEvent(String EventType, int id, LocalDate date){
        this.EventType=EventType;
        this.id=id;
        this.date=date;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public String getEventType() {
        return EventType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
