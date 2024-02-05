package com.techqwerty.cmsshoppingcart.models;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techqwerty.cmsshoppingcart.models.data.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByUsername(String username);
    
}
