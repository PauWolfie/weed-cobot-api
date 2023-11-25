package com.sacavix.telegramboot.dto;
import com.sacavix.telegramboot.model.User;

public class UserDTO extends User {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}