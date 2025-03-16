package com.school.controller;

import com.school.enums.Role;
import com.school.model.LoginRequest;
import com.school.model.LoginResponse;
import com.school.model.UserRequest;
import com.school.model.UserResponse;
import com.school.service.KafkaProducer;
import com.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@EnableCaching
public class UserController {

    private final UserService userService;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public UserController(UserService userService, KafkaProducer kafkaProducer) {

        this.userService = userService;
        this.kafkaProducer = kafkaProducer;
    }


//    ================= Register User API ======================

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody UserRequest userRequest) {
        UserResponse response = userService.registerUser(userRequest);
        System.out.println("Send message via kafka");
        kafkaProducer.sendMessage("user-registration", "New user registered: " + response.getId());
        return response;
    }

//    =================== Get User By User ID ================

    @Cacheable(value = "user", key = "#id")
    @GetMapping("/getUserById")
    public UserResponse getUser(@RequestParam Long id) {

        return userService.getUser(id);
    }

//    ================= Get User API ===================

    @GetMapping("/getUsersList")
    public List<UserResponse> getUserList(@RequestParam Role role) {

        return userService.getUserList(role);
    }

//    ================= Delete User API ===================

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {

        return userService.deleteUser(id);
    }

//    ================== Update User API ===============

    @PutMapping("/updateUser")
    public String updateUser(@RequestBody UserRequest userRequest) {

        return userService.updateUser(userRequest);
    }

//    ================ Student Login ====================

    @PostMapping("/{role}Login")
    public LoginResponse login(@PathVariable String role, @RequestBody LoginRequest loginRequest) {

        switch (role.toLowerCase()) {
            case "student":
                return userService.login(loginRequest, Role.STUDENT);
            case "teacher":
                return userService.login(loginRequest, Role.TEACHER);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
