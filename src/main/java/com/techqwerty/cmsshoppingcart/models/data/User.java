package com.techqwerty.cmsshoppingcart.models.data;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User implements UserDetails {

    private static final long serialVersionUID = 1l; 

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Size(min=2, message = "Username must be at least 2 characters long")
    @NotEmpty(message = "Username cannot be empty.")
    private String username;

    @Size(min=4, message = "Password must be at least 4 characters long")
    @NotEmpty(message = "Password cannot be empty.")
    private String password;

    @Transient
    @Size(min=4, message = "Confirm password must be at least 4 characters long")
    @NotEmpty(message = "Confirm password cannot be empty.")
    private String confirmPassword;

    @Email(message = "Please enter a valid email")
    private String email;

    @Column(name = "phone_number")
    @NotEmpty(message = "Phone number cannot be empty.")
    @Size(min=6, message = "Password must be at least 6 characters long")
    private String phoneNumber;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
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
