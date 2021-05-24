package com.example.notificationrecivetest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;

public class User {

    private String id;
    private String email;

    private int r_n;

    public int getR_n() {
        return r_n;
    }

    public void setR_n(int r_n) {
        this.r_n = r_n;
    }

    public User(String id, String email, int r_n) {
        this.id = id;
        this.email = email;
        this.r_n = r_n;
    }

    static void updateUser(DatabaseReference Ref, User user){
        Ref.setValue(user);
    }

    static void SubscribeNotification(DatabaseReference ref){
        ref.child("r_n").setValue(1);
        FirebaseMessaging.getInstance().subscribeToTopic("Notification");
    }
    static void unSubscribeNotification(DatabaseReference ref){
        ref.child("r_n").setValue(0);
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Notification");
    }
}

