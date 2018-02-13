package com.example.lenovo.trafficservices;

/**
 * Created by Nadine on 7/11/2017.
 */

public class ModelClassNotification {
    String notification, date;

    public ModelClassNotification(String notification, String date)
    {
        this.notification = notification;
        this.date = date;
    }

    public ModelClassNotification() {
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
