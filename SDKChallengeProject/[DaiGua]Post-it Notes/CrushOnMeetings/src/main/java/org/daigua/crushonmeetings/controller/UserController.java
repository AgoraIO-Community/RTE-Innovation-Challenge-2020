package org.daigua.crushonmeetings.controller;

import org.daigua.crushonmeetings.model.UserModel;
import org.daigua.crushonmeetings.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/login")
    public Object login(String username, String password) {
        Map<String, String> result = new HashMap<>();
        UserModel userModel = userRepo.findByUsername(username);
        if (userModel.getPassword().equals(password)) {
            result.put("result", "true");
            result.put("userid", userModel.getUid());
        } else
            result.put("result", "false");
        return result;
    }

}
