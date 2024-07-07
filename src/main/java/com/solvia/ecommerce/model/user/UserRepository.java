package com.solvia.ecommerce.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long>,  CrudRepository<User, Long> {

    User getByEmail(String email);
}
