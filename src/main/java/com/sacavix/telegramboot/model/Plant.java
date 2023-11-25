package com.sacavix.telegramboot.model;
public class Plant {
    private String id = null;
    private float air_humidity;
    private float soil_humidity;
    private float temperature;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getAir_humidity() {
        return air_humidity;
    }

    public void setAir_humidity(float air_humidity) {
        this.air_humidity = air_humidity;
    }

    public float getSoil_humidity() {
        return soil_humidity;
    }

    public void setSoil_humidity(float soil_humidity) {
        this.soil_humidity = soil_humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }


}
