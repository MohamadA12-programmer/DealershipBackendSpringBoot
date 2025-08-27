package com.example.demo.POJO;

import com.example.demo.entity.SalesPerson;

public class SalesPersonEvent {
        private String EventType;
        private int id;
        private String name;
        private String Email;

        public SalesPersonEvent(){}
        public SalesPersonEvent(String EventType, int id, String name, String Email){
            this.EventType=EventType;
            this.id=id;
            this.name=name;
            this.Email=Email;
        }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public String getEventType() {
        return EventType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
