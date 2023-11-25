package com.sacavix.telegramboot.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sacavix.telegramboot.dto.PlantDTO;
import com.sacavix.telegramboot.model.Plant;
import com.sacavix.telegramboot.model.User;
import com.sacavix.telegramboot.service.api.PlantServiceAPI;
import com.sacavix.telegramboot.service.api.UserServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/plant")
@CrossOrigin("*")
public class PlantController {

    @Autowired
    private PlantServiceAPI plantServiceAPI;
    @Autowired
    private UserServiceAPI userServiceApi;

    @GetMapping(value = "/all")
    public ResponseEntity<List<PlantDTO>> getAll(@RequestHeader String Authorization)
            throws Exception {
        List<PlantDTO> plants = plantServiceAPI.getAll();
        return new ResponseEntity<>(plants, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PlantDTO> getPlant(@RequestHeader String Authorization, @PathVariable String id)
            throws Exception {
        return new ResponseEntity<>(plantServiceAPI.get(id), HttpStatus.OK);
    }

    @PostMapping(value = "/user/{uid}")
    public ResponseEntity<Object> createUserNode(@RequestHeader String Authorization,
                                                 @RequestBody PlantDTO PlantInfo,
                                                 @PathVariable String uid)
            throws Exception {
        // Create Node
        plantServiceAPI.save(PlantInfo, PlantInfo.getId());
        User user = userServiceApi.get(uid);
        ArrayList<String> plantList = user.getPlants();
        plantList.add(PlantInfo.getId());
        user.setPlants(plantList);
        userServiceApi.save(user, uid);
        return new ResponseEntity<>("Plant created: " + PlantInfo.getId() + " for user: " + uid, HttpStatus.OK);
    }

    @DeleteMapping(value = "/user/{uid}/{id}")
    public ResponseEntity<Object> deletePlant(@RequestHeader String Authorization,
                                              @PathVariable String id,
                                              @PathVariable String uid)
            throws Exception {
        // Eliminar de la base de dades la planta
        plantServiceAPI.delete(id);
        User user = userServiceApi.get(uid);

        // Eliminar la planta del array de plantas del usuario
        ArrayList<String> plantList = user.getPlants();
        plantList.remove(id);
        user.setPlants(plantList);

        // Actualizar la informació del usuari en Firebase
        userServiceApi.save(user, uid);

        return new ResponseEntity<>("Plant deleted", HttpStatus.OK);

    }

    @PatchMapping(value = "/user/{uid}/{id}")
    public ResponseEntity<Object> updatePlant(@RequestHeader String Authorization,
                                              @RequestBody String plant,
                                              @PathVariable String id)

            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> bodyReq = objectMapper.readValue(plant, Map.class);

        // Create Node
        Plant updatedPlant =  patchPlant(plantServiceAPI.get(id), bodyReq);

        return new ResponseEntity<>("Plant updated", HttpStatus.OK);
    }
    private Plant patchPlant(PlantDTO plantToUpdate, Map<String, Object> reqBody) throws Exception {
        for (Map.Entry<String, Object> entry : reqBody.entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();

            try {
                Field field = Plant.class.getDeclaredField(attributeName);
                field.setAccessible(true);
                try {
                    field.set(plantToUpdate, attributeValue);
                } catch (Exception e) {
                    return null;
                }
            } catch (NoSuchFieldException e) {
                return null;
            }
        }

        return plantToUpdate;
    }
}