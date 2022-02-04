package net.dancier.dancer.authentication;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.dto.NewPasswortDto;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.service.AuthenticationService;
import net.dancier.dancer.core.controller.payload.ApiResponse;
import net.dancier.dancer.core.controller.payload.JwtAuthenticationResponse;
import net.dancier.dancer.core.controller.payload.LoginRequestDto;
import net.dancier.dancer.core.controller.payload.UserIdentityAvailability;
import net.dancier.dancer.core.exception.AppliationException;
import net.dancier.dancer.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Value("${app.redirectAfterEmailValidation}")
    String redirectAfterEmailValidation;

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        User result;
        try {
            result = authenticationService.registerUser(registerRequest);
        } catch (UserOrEmailAlreadyExistsException userOrEmailAlreadyExistsException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponse(false, "Username already exist"));
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                              HttpServletResponse httpServletResponse) {

        Authentication authentication = authenticationService.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsernameOrEmail(),
                        loginRequestDto.getPassword()
                )
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = authenticationService.getUser(userPrincipal.getId());
        if (!user.isEmailValidated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "You have to validate the email."));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = authenticationService.generateToken(authentication);
        Cookie cookie = new Cookie("jwt-token", jwt);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/email/validation")
    public ResponseEntity createEmailValidationCode(@NotNull @RequestBody String emailOrUsername) {
        log.info("sending mail for " + emailOrUsername);
        authenticationService.createEmailValidationCode(emailOrUsername);
        return ResponseEntity.ok().body(new ApiResponse(true, "ValidationCode send."));
    }

    @GetMapping("/email/validate/{validationCode}")
    public ResponseEntity emailValidation(@PathVariable String validationCode) {
        log.info("Got Validation code " + validationCode);
        authenticationService.checkEmailValidationCode(validationCode);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectAfterEmailValidation)).build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity resetPasswort(@RequestBody String userOrEmail) {
        authenticationService.createPasswordValidationCode(userOrEmail);
        return ResponseEntity.ok(new ApiResponse(true, "super"));
    }

    @GetMapping("/password/reset/{validationCode}")
    public ResponseEntity validatePassword(@PathVariable String validationCode) {
        String newPassword = authenticationService.checkPasswortCodeRequestAndCreateNew(validationCode);
        return ResponseEntity.ok(new NewPasswortDto(newPassword));
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        // TODO
        //Boolean isAvailable = !authenticationService.existsByUsername(username);
        return new UserIdentityAvailability(true);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !authenticationService.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
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