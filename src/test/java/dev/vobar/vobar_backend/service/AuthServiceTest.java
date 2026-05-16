package dev.vobar.vobar_backend.service;

import dev.vobar.vobar_backend.dto.LoginRequest;
import dev.vobar.vobar_backend.dto.LoginResponse;
import dev.vobar.vobar_backend.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_whenCredentialsValid_returnsToken() {
        // given
        LoginRequest request = new LoginRequest("admin", "password");
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin");
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtUtil.generate("admin")).thenReturn("jwt-token");

        // when
        LoginResponse response = authService.login(request);

        // then
        assertThat(response.token()).isEqualTo("jwt-token");
        verify(jwtUtil).generate("admin");
    }

    @Test
    void login_whenCredentialsInvalid_throwsUnauthorized() {
        // given
        LoginRequest request = new LoginRequest("admin", "wrong");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad credentials"));

        // when / then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ResponseStatusException.class);
        verify(jwtUtil, never()).generate(any());
    }
}
