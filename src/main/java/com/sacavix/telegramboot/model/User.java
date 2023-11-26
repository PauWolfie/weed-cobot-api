package com.sacavix.telegramboot.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id = null;
    private String email = null;
    private String name = null;
    private String surnames = null;
    private String telephone = null;
    private String chatId = null;
    private float max_air_humidity = -1;
    private float max_soil_humidity = -1;
    private float max_temperature = -1;
    private float min_air_humidity = -1;
    private float min_soil_humidity = -1;
    private float min_temperature = -1;
    private ArrayList<String> plants = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getMax_air_humidity() {
        return max_air_humidity;
    }
    public void setMax_air_humidity(float max_air_humidity) {
        this.max_air_humidity = max_air_humidity;
    }

    public float getMax_soil_humidity() {
        return max_soil_humidity;
    }

    public void setMax_soil_humidity(float max_soil_humidity) {
        this.max_soil_humidity = max_soil_humidity;
    }

    public float getMax_temperature() {
        return max_temperature;
    }

    public void setMax_temperature(float max_temperature) {
        this.max_temperature = max_temperature;
    }

    public float getMin_air_humidity() {
        return min_air_humidity;
    }

    public void setMin_air_humidity(float min_air_humidity) {
        this.min_air_humidity = min_air_humidity;
    }

    public float getMin_soil_humidity() {
        return min_soil_humidity;
    }

    public void setMin_soil_humidity(float min_soil_humidity) {
        this.min_soil_humidity = min_soil_humidity;
    }

    public float getMin_temperature() {
        return min_temperature;
    }

    public void setMin_temperature(float min_temperature) {
        this.min_temperature = min_temperature;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ArrayList<String> getPlants() {
        return plants;
    }

    public void setPlants(ArrayList<String> plants) {
        this.plants = plants;
    }
}
