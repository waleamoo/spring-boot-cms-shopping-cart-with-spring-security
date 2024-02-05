package com.techqwerty.cmsshoppingcart.models.data;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="admin")
@Data
public class Admin implements UserDetails {

    private static final long serialVersionUID = 2l; 

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   
    private String username;
    
    private String password;



    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    // @Override
    // public String getPassword() {
    //     // TODO Auto-generated method stub
    //     return null;
    // }

    // @Override
    // public String getUsername() {
    //     // TODO Auto-generated method stub
    //     return null;
    // }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
