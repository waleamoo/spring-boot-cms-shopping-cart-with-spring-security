package com.techqwerty.cmsshoppingcart.models.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name="categories")
@Data 
public class Category {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(min=2, message = "Name must be at least 2 characters long")
    @NotEmpty(message = "Name cannot be empty.")
	private String name;

	private String slug;
	
	private int sorting;
}
