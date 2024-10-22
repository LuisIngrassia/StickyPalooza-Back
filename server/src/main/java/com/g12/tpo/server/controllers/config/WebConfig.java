package com.g12.tpo.server.controllers.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("classpath:/static/uploads/")
                .addResourceLocations("file:/home/tomas/StickyPalooza-Back/server/src/main/resources/static/uploads/")
                .addResourceLocations("file:/home/juanchi/workspace/StickyPalooza-Back/server/src/main/resources/static/uploads/");
                
                // PROFE SI ESTAS LEYENDO ESTO POR FAVOR PERDON YA SE QUE ESTA MAL EL MANEJO DE IMAGENES 
                // CON UNA RUTA ASI PERO ES MOMENTANEO, VA A SER ARREGLADO SI O SI GRACIAS LE MANDO UN SALUDO!!
            }
}
