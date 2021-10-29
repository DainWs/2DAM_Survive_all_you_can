package com.joseduarte.dwssurviveallyoucan.models;

import com.joseduarte.dwssurviveallyoucan.firebase.FirebaseDBManager;

import java.util.Comparator;

public class User implements Comparable<User> {

    private String networkId = "NONE";
    private String mail = "";
    private String currentName = "User";
    private String photoURL = "";
    private long coins = 0;
    private boolean signIn;

    public User() {
    }

    public User(String networkId, String currentName, long coins) {
        this.networkId = networkId;
        this.currentName = currentName;
        this.coins = coins;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.networkId = FirebaseDBManager.makeFirebaseURLPath(mail);
        this.mail = mail;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    @Override
    public int compareTo(User o) {
        return (coins > o.coins) ? -1 :
               (o.coins > coins) ?  1 :
                currentName.compareToIgnoreCase(o.currentName);
    }


    public void setSignIn(boolean signIn) {
        this.signIn = signIn;
    }

    public boolean isSignIn() {
        return signIn;
    }

    @Override
    public String toString() {
        return "User{" +
                "networkId='" + networkId + '\'' +
                ", mail='" + mail + '\'' +
                ", currentName='" + currentName + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", coins=" + coins +
                ", signIn=" + signIn +
                '}';
    }
}
