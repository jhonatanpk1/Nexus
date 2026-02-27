package com.nexus.nexus.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nexus.nexus.controlador.AuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns(Arrays.asList(
                        "/",
                        "/login",
                        "/logout",
                        "/viaje/**",
                        "/vuelo/**",
                        "/vista/**",
                        "/reserva/**",
                        "/pago/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                ));
    }
}