package com.joyforliving.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionOpenApi {

    @Bean
    public OpenAPI documentacion() {
        return new OpenAPI().info(new Info()
                .title("Joy For Living Watersports & Activities API")
                .version("1.0.0")
                .description("Servicios REST que soportan el mapa de capacidades de la microempresa "
                        + "Joy For Living Watersports & Activities (Noord, Aruba).")
                .contact(new Contact().name("Benjamin Jair Stamper Alvarez - UTPL")));
    }
}
