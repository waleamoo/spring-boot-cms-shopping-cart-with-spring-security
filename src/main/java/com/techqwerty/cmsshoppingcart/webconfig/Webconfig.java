package com.techqwerty.cmsshoppingcart.webconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer {
	
	// explicit mapping - proper way to return a view - rather than having the controller do it 
	// @Override
	// public void addViewControllers(ViewControllerRegistry registry) {
	// 	WebMvcConfigurer.super.addViewControllers(registry);
	// 	registry.addViewController("/").setViewName("home");
	// }
	
	// copies the contents of the /resources to the class path when the app is running - for example, viewing a preview of an uploaded image 
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
			.addResourceHandler("/media/**")
			.addResourceLocations("file:/C:/Users/Ola/Documents/Spring Boot/cmsshoppingcart/src/main/resources/static/media/"); 
	}
}
