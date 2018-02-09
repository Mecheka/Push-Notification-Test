package com.example.suriya.firebasepushnotification.utill;

/**
 * Created by Suriya on 9/1/2561.
 */

public class User extends UserID {

    String name;
    String imageUrl;
    String token_id;

    public User(){

    }

    public User(String name, String imageUrl, String token_id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.token_id = token_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }
}
