package com.example.demo.POJO;

public class CustomerEvent {
    private String EventType;
    private int id;
    private String name;
    private String phone;


    public CustomerEvent(){
    }
    public CustomerEvent(String EventType, int id, String name, String phone){
        this.EventType=EventType;
        this.id=id;
        this.name=name;
        this.phone=phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
