package com.solvia.ecommerce.application.controllers;

import com.solvia.ecommerce.model.user.User;
import com.solvia.ecommerce.model.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

@BasePathAwareController
public class CreateUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CreateUserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            userRepository.save(user);
        }catch (DataIntegrityViolationException exception){
            throw new ResponseStatusException(
                  HttpStatus.BAD_REQUEST, "That email address is already in use.");
        }

        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }
}
