package com.joyforliving.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS para el modo de desarrollo (Vite en el puerto 5173) y reenvio de las
 * rutas del enrutador de React al index.html empaquetado dentro del JAR.
 */
@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {

    /** Rutas que atiende React Router del lado del navegador. */
    private static final String[] RUTAS_SPA = {
            "/panel", "/experiencias", "/reservas", "/clientes", "/equipos"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("http://localhost:*", "https://*.up.railway.app", "https://*.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Al recargar el navegador en /reservas, el servidor devuelve el index.html
        // y React Router resuelve la vista del lado del cliente.
        for (String ruta : RUTAS_SPA) {
            registry.addViewController(ruta).setViewName("forward:/index.html");
        }
    }
}
