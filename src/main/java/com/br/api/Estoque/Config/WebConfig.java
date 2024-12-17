package com.br.api.Estoque.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebConfig implements WebMvcConfigurer {

    // MAPEANDO OS METODOS E AUTORIZAÇÕES
    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With");
    }
}