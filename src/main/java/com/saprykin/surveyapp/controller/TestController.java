package com.saprykin.surveyapp.controller;

import com.saprykin.surveyapp.model.User;
import com.saprykin.surveyapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private UserService userService;
    @RequestMapping("/users")
    public User getPersonDetail() {
        List<User> users = userService.findAllUsers();
        return users.get(0);
    }
}
