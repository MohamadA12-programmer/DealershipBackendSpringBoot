package com.example.demo.POJO;

public class UserEvent {
    private String EventType;
    private int id;
    private String username;
    private String role;

    public UserEvent(){
    }
    public UserEvent(String EventType, int id, String username, String role){
        this.EventType=EventType;
        this.id=id;
        this.username=username;
        this.role=role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
