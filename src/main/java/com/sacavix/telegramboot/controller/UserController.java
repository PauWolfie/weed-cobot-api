package com.sacavix.telegramboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sacavix.telegramboot.dto.UserDTO;
import com.sacavix.telegramboot.model.User;
import com.sacavix.telegramboot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceAPI;

    @GetMapping(value = "/all")
    public ResponseEntity<List<UserDTO>> getAll(@RequestHeader String Authorization)
            throws Exception {
        List<UserDTO> users = userServiceAPI.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUser(@RequestHeader String Authorization, @PathVariable String id)
            throws Exception {
        return new ResponseEntity<>(userServiceAPI.get(id), HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> createUserNode(@RequestHeader String Authorization,
                                                 @RequestBody User UserInfo)
            throws Exception {
        // Create Node
        String uid = userServiceAPI.save(UserInfo);
        return new ResponseEntity<>("User created: " + uid, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Object> updateUser(@RequestHeader String Authorization, @PathVariable String id,
                                             @RequestBody String user)
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> bodyReq = objectMapper.readValue(user, Map.class);

        // Create Node
        User updatedUser = patchUser(userServiceAPI.get(id), bodyReq);
        userServiceAPI.save(updatedUser, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteUser(@RequestHeader String Authorization, @PathVariable String id)
            throws Exception {
        userServiceAPI.delete(id);
        return new ResponseEntity<>("User deleted", HttpStatus.OK);
    }

    private User patchUser(User userToUpdate, Map<String, Object> reqBody) throws Exception {
        for (Map.Entry<String, Object> entry : reqBody.entrySet()) {
            String attributeName = entry.getKey();
            Object attributeValue = entry.getValue();

            try {
                Field field = User.class.getDeclaredField(attributeName);
                field.setAccessible(true);
                try {
                    field.set(userToUpdate, attributeValue);
                } catch (Exception e) {
                    return null;
                }
            } catch (NoSuchFieldException e) {
                return null;
            }
        }

        return userToUpdate;
    }


}
