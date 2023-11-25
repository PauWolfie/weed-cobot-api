package com.sacavix.telegramboot.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email = null;
    private String name = null;
    private String surnames = null;
    private String telephone = null;
    private ArrayList<String> plants = null;

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
