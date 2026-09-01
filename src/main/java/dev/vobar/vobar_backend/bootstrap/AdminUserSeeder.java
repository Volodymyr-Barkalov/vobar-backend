package dev.vobar.vobar_backend.bootstrap;

import dev.vobar.vobar_backend.model.User;
import dev.vobar.vobar_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminUserSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminUserSeeder.class);

    private final UserRepository userRepository;
    private final String username;
    private final String passwordHash;

    public AdminUserSeeder(UserRepository userRepository,
                           @Value("${app.admin.username:}") String username,
                           @Value("${app.admin.password-hash:}") String passwordHash) {
        this.userRepository = userRepository;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (username.isBlank() || passwordHash.isBlank()) {
            log.info("Admin seeding skipped: ADMIN_USERNAME / ADMIN_PASSWORD_HASH not set");
            return;
        }

        if (!isBcryptHash(passwordHash)) {
            log.error("Admin seeding skipped: ADMIN_PASSWORD_HASH is not a BCrypt hash "
                    + "(expected it to start with $2a$, $2b$ or $2y$). Refusing to store it.");
            return;
        }

        long existing = userRepository.count();
        if (existing > 0) {
            log.info("Admin seeding skipped: users collection already holds {} document(s)", existing);
            return;
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passwordHash);
        userRepository.save(admin);
        log.info("Seeded admin user '{}'", username);
    }

    private static boolean isBcryptHash(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
}
