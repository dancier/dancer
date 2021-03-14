package de.frubumi.dance.dancer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpringDataJpaUserDetailsServiceTest {

    DancerUserRepository repository = mock(DancerUserRepository.class);
    SpringDataJpaUserDetailsService service;

    @BeforeEach
    void setUp() {
        service = new SpringDataJpaUserDetailsService(repository);
    }

    @Nested
    @DisplayName("Given user Marc exists")
    class GivenUserMarcExists {

        @BeforeEach
        void setUp() {
            when(repository.findByName("Marc")).thenReturn(new DancerUser("Marc", "Gorzala", "ROLE_MANAGER"));
        }

        @Test
        @DisplayName("then user details for Marc can be loaded")
        void thenUserDetailsForMarcCanBeLoaded() {
            assertThat(service.loadUserByUsername("Marc").getUsername()).isEqualTo("Marc");
        }

        @Test
        @DisplayName("then user details for Dominik can't be found")
        void thenUserDetailsForDominikCantBeFound() {
            Throwable thrown = catchThrowable(() -> service.loadUserByUsername("Dominik"));
            assertThat(thrown).isInstanceOf(UsernameNotFoundException.class);
        }

    }

}