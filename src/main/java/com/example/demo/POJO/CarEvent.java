package com.example.demo.POJO;

public class CarEvent {
        private String eventType;
        private int carId;
        private String make;
        private String model;

        public CarEvent() {}
        public CarEvent(String eventType, int carId, String make, String model) {
            this.eventType = eventType;
            this.carId = carId;
            this.make = make;
            this.model= model;
        }

        public void setEventType(String eventType){
            this.eventType=eventType;
        }
        public String getEventType(){
            return eventType;
        }

        public void setCarId(int carId){
            this.carId=carId;
        }
        public int getCarId(){
            return carId;
        }
        public void setMake(String make){
            this.make=make;
        }
        public String getMake(){
            return make;
        }
        public String getModel(){
            return model;
        }
        public void setModel(String model){
            this.model=model;
        }
    }

