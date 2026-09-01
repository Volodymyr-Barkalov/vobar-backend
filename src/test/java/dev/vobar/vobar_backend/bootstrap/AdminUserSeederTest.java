package dev.vobar.vobar_backend.bootstrap;

import dev.vobar.vobar_backend.model.User;
import dev.vobar.vobar_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserSeederTest {

    private static final String HASH = "$2a$10$7EqJtq98hPqEX7fNZaFWoOa1u8.uJ9wGZ0y0zvV3lQ2Bx1Kk1YqjW";

    @Mock
    private UserRepository userRepository;

    private AdminUserSeeder seeder(String username, String passwordHash) {
        return new AdminUserSeeder(userRepository, username, passwordHash);
    }

    @Test
    void run_whenCredentialsUnset_doesNotTouchTheDatabase() {
        // given
        AdminUserSeeder seeder = seeder("", "");

        // when
        seeder.run(null);

        // then
        verifyNoInteractions(userRepository);
    }

    @Test
    void run_whenOnlyUsernameSet_doesNotTouchTheDatabase() {
        // given
        AdminUserSeeder seeder = seeder("admin", "");

        // when
        seeder.run(null);

        // then
        verifyNoInteractions(userRepository);
    }

    @Test
    void run_whenHashIsNotBcrypt_refusesToStoreIt() {
        // given
        AdminUserSeeder seeder = seeder("admin", "plaintext-password");

        // when
        seeder.run(null);

        // then
        verifyNoInteractions(userRepository);
    }

    @Test
    void run_whenUsersAlreadyExist_leavesThemAlone() {
        // given
        when(userRepository.count()).thenReturn(1L);
        AdminUserSeeder seeder = seeder("admin", HASH);

        // when
        seeder.run(null);

        // then
        verify(userRepository).count();
        verify(userRepository, never()).save(any());
    }

    @Test
    void run_whenCollectionIsEmpty_savesTheAdminUser() {
        // given
        when(userRepository.count()).thenReturn(0L);
        AdminUserSeeder seeder = seeder("admin", HASH);

        // when
        seeder.run(null);

        // then
        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(saved.capture());
        assertThat(saved.getValue().getUsername()).isEqualTo("admin");
        assertThat(saved.getValue().getPassword()).isEqualTo(HASH);
        assertThat(saved.getValue().getId()).isNull();
    }
}
