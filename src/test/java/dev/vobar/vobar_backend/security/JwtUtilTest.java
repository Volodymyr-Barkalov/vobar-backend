package dev.vobar.vobar_backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private static final String SECRET = "dGVzdFNlY3JldEtleVRoYXRJc0xvbmdFbm91Z2gxMjM=";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET, 86400000L);
    }

    @Test
    void generate_returnsNonBlankToken() {
        // given / when
        String token = jwtUtil.generate("admin");

        // then
        assertThat(token).isNotBlank();
    }

    @Test
    void extractUsername_fromGeneratedToken_returnsCorrectUsername() {
        // given
        String token = jwtUtil.generate("admin");

        // when
        String username = jwtUtil.extractUsername(token);

        // then
        assertThat(username).isEqualTo("admin");
    }

    @Test
    void validate_withValidToken_returnsTrue() {
        // given
        String token = jwtUtil.generate("admin");

        // when / then
        assertThat(jwtUtil.validate(token)).isTrue();
    }

    @Test
    void validate_withGarbageToken_returnsFalse() {
        // given / when / then
        assertThat(jwtUtil.validate("not.a.valid.token")).isFalse();
    }

    @Test
    void validate_withExpiredToken_returnsFalse() {
        // given — negative expiration means token is already expired at creation time
        JwtUtil expiredUtil = new JwtUtil(SECRET, -1000L);
        String expiredToken = expiredUtil.generate("admin");

        // when / then
        assertThat(jwtUtil.validate(expiredToken)).isFalse();
    }
}
