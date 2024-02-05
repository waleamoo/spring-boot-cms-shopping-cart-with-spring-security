package com.techqwerty.cmsshoppingcart.models.data;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min=2, message = "Name must be at least 2 characters long")
    @NotEmpty(message = "Name cannot be empty.")
    private String name;
    private String slug;
    @Size(min=5, message = "Description must be at least 2 characters long")
    @NotEmpty(message = "Description cannot be empty.")
    private String description;
    private String image;
    @Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?", message = "Expected format: 5, 5.99, 15.99") // 29.99
    @NotEmpty(message = "Price cannot be empty.")
    private String price;
    @Pattern(regexp = "^[1-9][0-9]*", message = "Please choose a category") 
    @Column(name = "category_id")
    private String categoryId;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
