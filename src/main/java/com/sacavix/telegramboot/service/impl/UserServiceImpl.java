package com.sacavix.telegramboot.service.impl;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.sacavix.telegramboot.commons.GenericServiceImpl;
import com.sacavix.telegramboot.dto.UserDTO;
import com.sacavix.telegramboot.model.User;
import com.sacavix.telegramboot.service.api.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserDTO> implements UserServiceAPI {

    @Autowired
    private Firestore firestore;

    @Override
    public CollectionReference getCollection() {
        return firestore.collection("usuaris");
    }
}