package net.dancier.dancer.authentication;

import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.event.NewUserCreatedEvent;
import net.dancier.dancer.authentication.model.*;
import net.dancier.dancer.authentication.repository.EmailValidationCodeRepository;
import net.dancier.dancer.authentication.repository.PasswordResetCodeRepository;
import net.dancier.dancer.authentication.repository.RoleRepository;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.authentication.service.AuthenticationService;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.mail.service.MailCreationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisherMock;
    @Mock
    private RoleRepository roleRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private MailCreationService mailCreationService;

    @Mock
    private EmailValidationCodeRepository validationCodeRepositoryMock;

    @Mock
    private PasswordResetCodeRepository passwordResetCodeRepositoryMock;

    private final UUID userId = UUID.randomUUID();

    @InjectMocks
    private AuthenticationService underTest;

    @BeforeEach
    void init() {
    }

    @Test
    void getExistingUser_returns_user() {
        when(userRepositoryMock.getReferenceById(userId)).thenReturn(dummyUser());

        User returnedUser = underTest.getUser(userId);

        assertThat(returnedUser).isNotNull();
    }

    @Test
    void attemptToGetNotExistingUser_throwsExeption() {
        when(userRepositoryMock.getReferenceById(userId)).thenThrow(EntityNotFoundException.class);
        assertThrows(NotFoundException.class, () -> underTest.getUser(userId));
    }

    @Test
    void register() {
        when(userRepositoryMock.findByEmail(
                dummyUser().getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoderMock.encode(any())).thenReturn("bar");
        when(roleRepositoryMock.findByName(any())).thenReturn(Optional.of(dummyRole()));
        when(userRepositoryMock.save(any())).thenReturn(dummyUser(true));

        underTest.registerUser(dummyRegisterRequestDto(dummyUser()));

        ArgumentCaptor<NewUserCreatedEvent> newUserCreatedEventArgumentCaptor = ArgumentCaptor.forClass(NewUserCreatedEvent.class);
        verify(applicationEventPublisherMock).publishEvent(newUserCreatedEventArgumentCaptor.capture());
        assertThat(newUserCreatedEventArgumentCaptor.getValue().getId()).isNotNull();
        verify(userRepositoryMock).save(any());
    }

    @Test
    void mailIsEnqueudWhenAccountAlreadyExists() {
        when(userRepositoryMock.findByEmail(
                dummyUser().getEmail()))
                .thenReturn(Optional.of(dummyUser(true)));

        when(mailCreationService.createDancierMessageFromTemplate(any(), any(), any(), any(), any())).thenReturn(new SimpleMailMessage());

        underTest.registerUser(dummyRegisterRequestDto(dummyUser()));

        verify(applicationEventPublisherMock, times(1)).publishEvent(any(SimpleMailMessage.class));
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
        User existingUser = dummyUser(true);

        String optionalCode = underTest.createPasswordResetCode(existingUser);

        assertThat(optionalCode).isNotNull();
    }

    @Test
    void checkPasswortCodeRequestAndCreateNewPassword() {
        String newPasswordCode = "foo";
        String newPassword = "bar";

        when(passwordResetCodeRepositoryMock.findByCode(newPasswordCode))
                .thenReturn(Optional.of(dummyPasswortResetCode(newPasswordCode)));
        when(userRepositoryMock.getReferenceById(userId)).thenReturn(dummyUser(true));

        underTest.checkPasswortCodeRequestAndCreateNew(newPasswordCode, newPassword);

        verify(userRepositoryMock, times(1)).save(any());
    }

    private User dummyUser() {
        return dummyUser(false);
    }

    private User dummyUser(Boolean savedUser) {
        User user = new User("info@foo.de", "secret");
        if (savedUser) {
            user.setId(userId);
            user.setEmailValidated(true);
        }
        return user;
    }

    private RegisterRequestDto  dummyRegisterRequestDto(User user) {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
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