package com.example.lenovo.trafficservices;



public class ModelClassCarOwner {
    String userName,image, carNumber, carPrice, carSeats, phoneNumber, time, userUid, carImage, from, to, date;

    public ModelClassCarOwner(String image, String userName, String carNumber, String carPrice,
                              String carSeats, String phoneNumber, String time, String userUid,
                              String carImage, String from, String to, String date) {
        this.image = image;
        this.userName = userName;
        this.carNumber = carNumber;
        this.carPrice = carPrice;
        this.carSeats = carSeats;
        this.phoneNumber = phoneNumber;
        this.time = time;
        this.userUid = userUid;
        this.carImage = carImage;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public ModelClassCarOwner() {
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }
    public String getCarNumber() {
        return carNumber;
    }

    public void setCarPrice(String carPrice) {
        this.carPrice = carPrice;
    }
    public String getCarPrice() {
        return carPrice;
    }
    public void setCarSeats(String carSeats) {
        this.carSeats = carSeats;
    }
    public String getCarSeats() {
        return carSeats;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTime() {
        return time;
    }


    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getUserUid() {
        return userUid;
    }
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getCarImage() {
        return carImage;
    }
    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public  void setFrom(String from){
        this.from = from;
    }
    public String getFrom(){
        return from;
    }

    public void setTo(String to){
        this.to = to;
    }
    public String getTo(){
        return to;
    }

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return date;
    }

}
