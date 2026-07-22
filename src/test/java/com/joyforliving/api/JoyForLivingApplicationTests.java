package com.joyforliving.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class JoyForLivingApplicationTests {

    @Test
    @DisplayName("El contexto de Spring arranca con el perfil de desarrollo")
    void elContextoArranca() {
        // Prueba de humo ejecutada en cada corrida del pipeline de integracion continua.
    }
}
