package com.techqwerty.cmsshoppingcart.models;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techqwerty.cmsshoppingcart.models.data.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    User findByUsername(String username);
}
