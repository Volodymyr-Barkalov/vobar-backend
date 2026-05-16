package dev.vobar.vobar_backend;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class BcryptGeneratorTest {

    @Test
    void generate() {
        System.out.println(new BCryptPasswordEncoder().encode("my-password"));
    }
}
