package net.dancier.dancer.authentication.service;

import net.dancier.dancer.authentication.UserOrEmailAlreadyExistsException;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.Role;
import net.dancier.dancer.authentication.model.RoleName;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.model.ValidationCode;
import net.dancier.dancer.authentication.repository.PasswordResetCodeRepository;
import net.dancier.dancer.authentication.repository.RoleRepository;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.authentication.repository.ValidationCodeRepository;
import net.dancier.dancer.core.exception.AppException;
import net.dancier.dancer.core.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    //https://reflectoring.io/unit-testing-spring-boot/

    @InjectMocks
    private AuthenticationService underTest;

    @Mock
    private RoleRepository roleRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private ValidationCodeRepository validationCodeRepositoryMock;

    @Mock
    private PasswordResetCodeRepository passwordResetCodeRepositoryMock;

    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void init() {
    }

    @Test
    void getExistingUser_returns_user() {
        when(userRepositoryMock.getById(userId)).thenReturn(dummyUser());

        User returnedUser = underTest.getUser(userId);

        assertThat(returnedUser).isNotNull();
    }

    @Test
    void attemptToGetNotExistingUser_throwsExeption() {
        when(userRepositoryMock.getById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(NotFoundException.class, () -> underTest.getUser(userId));
    }

    @Test
    void register() {
        when(userRepositoryMock.findByUsernameOrEmail(
                dummyUser().getUsername(),
                dummyUser().getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoderMock.encode(any())).thenReturn("bar");
        when(roleRepositoryMock.findByName(any())).thenReturn(Optional.of(dummyRole()));
        when(userRepositoryMock.save(any())).thenReturn(dummyUser());

        User user = underTest.registerUser(dummyRegisterRequestDto(dummyUser()));

        assertThat(user).isNotNull();

        verify(validationCodeRepositoryMock).save(any());
        verify(userRepositoryMock).save(any());
    }

    @Test
    void registerThrowsExceptionWhenUserAlreadyExists() {
        when(userRepositoryMock.findByUsernameOrEmail(
                dummyUser().getUsername(),
                dummyUser().getEmail()))
                .thenReturn(Optional.of(dummyUser()));

        assertThrows(UserOrEmailAlreadyExistsException.class,
                () -> underTest.registerUser(dummyRegisterRequestDto(dummyUser())));
    }

    @Test
    void createEmailValidationCodeForUser() {

    }




    private User dummyUser() {
        return new User("foo", "bar", "info@foo.de", "secret");
    }

    private RegisterRequestDto  dummyRegisterRequestDto(User user) {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setName(user.getName());
        registerRequestDto.setUsername(user.getUsername());
        registerRequestDto.setEmail(user.getEmail());
        registerRequestDto.setPassword(user.getPassword());
        return registerRequestDto;
    }

    private Role dummyRole() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName(RoleName.ROLE_USER);
        return role;
    }
}