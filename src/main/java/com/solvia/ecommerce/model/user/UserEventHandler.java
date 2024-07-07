package com.solvia.ecommerce.model.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class UserEventHandler {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserEventHandler(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @HandleBeforeCreate
    public void handleUserCreate(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @HandleBeforeSave
    public void handleUserUpdate(User user) {

        entityManager.detach(user);
        User oldUser = userRepository.getReferenceById(user.getId());
        String oldPassword = oldUser.getPassword();
        String newPassword = user.getPassword();
        String newPasswordEncoded = passwordEncoder.encode(newPassword);

        if (!newPasswordEncoded.equals(oldPassword) && !newPassword.equals(oldPassword)) // When patch sends body without password, new password is old password.
            user.setPassword(passwordEncoder.encode(user.getPassword()));

    }

}