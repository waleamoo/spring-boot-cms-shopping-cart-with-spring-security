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
@Table(name="pages")
@Data 
public class Page {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(min=2, message = "Title must be at least 2 characters long")
    @NotEmpty(message = "Title cannot be empty.")
	private String title;
	private String slug;
	@Size(min=5, message = "Content must be at least 5 characters long")
	private String content;
	private int sorting;
}
