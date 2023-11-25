package com.sacavix.telegramboot.dto;

import com.sacavix.telegramboot.model.Plant;

public class PlantDTO extends Plant {

    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
