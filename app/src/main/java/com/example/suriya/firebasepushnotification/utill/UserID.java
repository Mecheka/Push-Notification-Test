package com.example.suriya.firebasepushnotification.utill;

import android.support.annotation.NonNull;

/**
 * Created by Suriya on 10/1/2561.
 */

public class UserID {

    public String userID;

    public <T extends UserID> T withId(@NonNull final String id){
        this.userID = id;
        return (T) this;
    }
}
