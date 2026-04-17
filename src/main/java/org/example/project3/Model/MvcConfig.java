package org.example.project3.Model;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This maps the URL "/uploads/**" to the physical folder "uploads"
        registry.addResourceHandler("/uploads2/**")
                .addResourceLocations("file:uploads2/");
    }
}

