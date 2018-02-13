package com.example.lenovo.trafficservices;

/**
 * Created by LENOVO on 11/04/2017.
 */

public class ModelClass {
    String userName,image, userUid, from, to, date, time, phoneNumber;

    public ModelClass(String image, String userName, String userUid,
                String from, String to, String date, String time, String phoneNumber)
    {
        this.image = image;
        this.userName = userName;
        this.userUid = userUid;
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
        this.phoneNumber = phoneNumber;
    }

    public ModelClass() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
