package com.g12.tpo.server.controllers.config; // Ensure this matches your directory structure

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static resources from the relative path
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./path/to/your/upload/"); // Relative path to your images
    }
}
