package com.swanhtetaungphyo.article_approval.controller;

import com.swanhtetaungphyo.article_approval.dto.UserDto;
import com.swanhtetaungphyo.article_approval.services.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private  final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?>   CreateUser(
            @RequestBody UserDto userDto
            ){
        boolean condition = userService.CreateUser(userDto);
        return condition ? ResponseEntity.ok().body("User is created successfully")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed");

    }
}
