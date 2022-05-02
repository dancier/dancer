package net.dancier.dancer.authentication;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.dto.NewPasswortDto;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.service.AuthenticationService;
import net.dancier.dancer.core.controller.payload.ApiResponse;
import net.dancier.dancer.core.controller.payload.JwtAuthenticationResponse;
import net.dancier.dancer.core.controller.payload.LoginRequestDto;
import net.dancier.dancer.core.exception.AppliationException;
import net.dancier.dancer.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private static Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        try {
            authenticationService.registerUser(registerRequest);
        } catch (UserOrEmailAlreadyExistsException userOrEmailAlreadyExistsException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponse(false, "Email address already exist"));
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/profile")
                .build()
                .toUri();
        return ResponseEntity
                .created(location)
                .body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                       HttpServletResponse httpServletResponse) {
        Authentication authentication = authenticationService.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        User user = authenticationService.getUser(authenticatedUser.getId());
        if (!user.isEmailValidated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "You have to validate the email."));
        }
        String jwt = authenticationService.generateToken(authentication);
        Cookie cookie = authenticationService.generateCookie(jwt);
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse httpServletResponse) {
        Cookie cookie = authenticationService.generateCookie(null);
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/validation")
    public ResponseEntity createEmailValidationCode(@NotNull @RequestBody String emailAddress) {
        log.info("sending mail for " + emailAddress);
        authenticationService.createEmailValidationCode(emailAddress.trim());
        return ResponseEntity.ok()
                .body(new ApiResponse(true, "ValidationCode send."));
    }

    @GetMapping("/email/validate/{validationCode}")
    public ResponseEntity emailValidation(@PathVariable String validationCode, HttpServletResponse httpServletResponse) {
        User validatedUser = authenticationService.checkEmailValidationCode(validationCode);
        Cookie cookie = authenticationService
                .generateCookie(authenticationService.generateToken(validatedUser.getId().toString()));
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(new ApiResponse(true, "Validated and logged in"));
    }

    @PostMapping("/password/reset")
    public ResponseEntity createPasswortResetCode(@RequestBody String userOrEmail) {
        authenticationService.createPasswordResetCode(userOrEmail);
        return ResponseEntity.ok(new ApiResponse(true, "super"));
    }

    @GetMapping("/password/reset/{validationCode}")
    public ResponseEntity validatePassword(@PathVariable String validationCode) {
        String newPassword = authenticationService.checkPasswortCodeRequestAndCreateNew(validationCode);
        return ResponseEntity.ok(new NewPasswortDto(newPassword));
    }

    @GetMapping("/checkEmailAvailability/{email}")
    public ResponseEntity<?> checkEmailAvailability(@PathVariable String email) {
        return ResponseEntity.ok(!authenticationService.existsByEmail(email));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AppliationException.class)
    public String handle(AppliationException ae) {
        return ae.getLocalizedMessage();
    }
}