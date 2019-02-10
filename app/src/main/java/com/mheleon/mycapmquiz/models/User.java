package com.mheleon.mycapmquiz.models;

import io.realm.RealmObject;

public class User extends RealmObject {
    private String nickname;

    public User(String nickname) {
        this.nickname = nickname;
    }

    public User() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
