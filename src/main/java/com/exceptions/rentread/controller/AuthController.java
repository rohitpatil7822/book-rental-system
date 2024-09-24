package com.exceptions.rentread.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.exceptions.rentread.dto.UserDTO;
import com.exceptions.rentread.entity.User;
import com.exceptions.rentread.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user){

        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }


    @GetMapping("/getUser/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        User user = userService.loadUserByUsername(email);

        UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);



    }

}
