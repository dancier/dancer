package net.dancier.dancer.authentication.service;

import net.dancier.dancer.authentication.UserOrEmailAlreadyExistsException;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.*;
import net.dancier.dancer.authentication.repository.PasswordResetCodeRepository;
import net.dancier.dancer.authentication.repository.RoleRepository;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.authentication.repository.EmailValidationCodeRepository;
import net.dancier.dancer.core.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    private EmailValidationCodeRepository validationCodeRepositoryMock;

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
        User userToHaveItsEmailValidated = dummyUser(true);

        when(validationCodeRepositoryMock
                .findByUserId(userToHaveItsEmailValidated.getId()))
                .thenReturn(Optional.empty());

        underTest.createEmailValidationCode(userToHaveItsEmailValidated);
        verify(validationCodeRepositoryMock).save(any());
    }

    @Test
    void createEmailValidationCodeForUnsavedUser() {
        User userToHaveItsEmailValidated = dummyUser(false);

        assertThrows(NullPointerException.class,
                () -> underTest.createEmailValidationCode(userToHaveItsEmailValidated));
    }

    @Test
    void checkEmailValidationCode() {
        String dummyEmailValidationCode = "foo";
        when(validationCodeRepositoryMock.findByCode(dummyEmailValidationCode))
                .thenReturn(
                        Optional.of(
                                dummyValidationCode(dummyEmailValidationCode)
                        )
                );
        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(dummyUser(true)));

        underTest.checkEmailValidationCode(dummyEmailValidationCode);

        verify(validationCodeRepositoryMock).delete(any());
        verify(userRepositoryMock).save(any());
    }

    @Test
    void createPasswordValidationCode() {
        User userToHaveAPasswordValidationCode = dummyUser(true);

        when(userRepositoryMock.findByUsernameOrEmail(any(),any()))
                .thenReturn(Optional.of(userToHaveAPasswordValidationCode));

        String passwordValidationCode = underTest.createPasswordValidationCode(userToHaveAPasswordValidationCode.getUsername());

        assertThat(passwordValidationCode).isNotNull();
    }

    @Test
    void checkPasswortCodeRequestAndCreateNewPassword() {
        String newPasswortCode = "foo";

        when(passwordResetCodeRepositoryMock.findByCode(newPasswortCode))
                .thenReturn(Optional.of(dummyPasswortResetCode(newPasswortCode)));
        when(userRepositoryMock.getById(userId)).thenReturn(dummyUser(true));

        String newPassword = underTest.checkPasswortCodeRequestAndCreateNew(newPasswortCode);

        assertThat(newPassword).isNotNull();
    }

    private User dummyUser() {
        return dummyUser(false);
    }

    private User dummyUser(Boolean savedUser) {
        User user = new User("foo", "bar", "info@foo.de", "secret");
        if (savedUser) {
            user.setId(userId);
        }
        return user;
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

    private EmailValidationCode dummyValidationCode(String code) {
        EmailValidationCode emailValidationCode = new EmailValidationCode();
        emailValidationCode.setUserId(userId);
        emailValidationCode.setCode(code);
        emailValidationCode.setExpiresAt(Instant.now().plus(3, ChronoUnit.HOURS));
        return emailValidationCode;
    }

    private PasswordResetCode dummyPasswortResetCode(String code) {
        PasswordResetCode passwordResetCode = new PasswordResetCode();
        passwordResetCode.setUserId(userId);
        passwordResetCode.setCode(code);
        return passwordResetCode;
    }
}