package com.chochoChat.Modal;

public class UserInfoModal {

    String City;
    String Name;
    String FCM;
    String Image;
    int Fav;
    public UserInfoModal() {
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFCM() {
        return FCM;
    }

    public void setFCM(String FCM) {
        this.FCM = FCM;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getFav() {
        return Fav;
    }

    public void setFav(int fav) {
        Fav = fav;
    }
}