package com.exceptions.rentread.dto;

import com.exceptions.rentread.entity.enums.Role;

import lombok.Data;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

    // Constructor
    public UserDTO(String firstName, String lastName, String email , Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}
