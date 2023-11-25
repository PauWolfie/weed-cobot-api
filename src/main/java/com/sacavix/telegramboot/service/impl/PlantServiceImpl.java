package com.sacavix.telegramboot.service.impl;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.sacavix.telegramboot.commons.GenericServiceImpl;
import com.sacavix.telegramboot.dto.PlantDTO;
import com.sacavix.telegramboot.model.Plant;
import com.sacavix.telegramboot.service.api.PlantServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantServiceImpl  extends GenericServiceImpl<Plant, PlantDTO> implements PlantServiceAPI {

    @Autowired
    private Firestore firestore;

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("plantes");
    }
}