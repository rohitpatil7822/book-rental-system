package com.exceptions.rentread.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.exceptions.rentread.dto.UserDTO;
import com.exceptions.rentread.entity.User;
import com.exceptions.rentread.entity.enums.Role;
import com.exceptions.rentread.exceptions.ResourceAlreadyExistsException;
import com.exceptions.rentread.exceptions.ResourceNotFoundException;
import com.exceptions.rentread.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserDTO registerUser(User user){

        if (userRepository.existsByEmail(user.getEmail())) {
            
            throw new ResourceAlreadyExistsException(HttpStatus.CONFLICT,"Email: "+user.getEmail()+" is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : Role.USER);
        User savedUser = userRepository.save(user);

        return new UserDTO(savedUser.getFirstName(), savedUser.getLastName(), savedUser.getEmail(),savedUser.getRole());

    }

    public User loadUserByUsername(String email) throws ResourceNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND,"User with email " + email + " not found"));
    }
}
