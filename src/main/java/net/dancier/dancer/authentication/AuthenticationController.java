package net.dancier.dancer.authentication;

import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.service.AuthenticationService;
import net.dancier.dancer.controller.payload.ApiResponse;
import net.dancier.dancer.controller.payload.JwtAuthenticationResponse;
import net.dancier.dancer.controller.payload.LoginRequest;
import net.dancier.dancer.controller.payload.SignUpRequest;
import net.dancier.dancer.security.JwtTokenProvider;
import net.dancier.dancer.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
import java.util.UUID;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private static Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = authenticationService.getUser(userPrincipal.getId());
        if (!user.isEmailValidated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(false, "You have to validate the email."));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        Cookie cookie = new Cookie("jwt-token", jwt);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/reset")
    public ResponseEntity resetPasswort(@RequestBody String userOrEmail) {
        // send Mail with link
        authenticationService.createPasswordValidationCodeForUserOrEmail(userOrEmail);
        return ResponseEntity.ok(new ApiResponse(true, "super"));
    }

    @GetMapping("/reset/validate/{validationCode}")
    public ResponseEntity validatePassword(@PathVariable String validationCode) {
        return null;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info("Checking for existing user: " + signUpRequest.getUsername());
        User result;
        try {
            result = authenticationService.registerUser(signUpRequest);
        } catch (UserOrEmailAlreadyExistsException userOrEmailAlreadyExistsException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "Username already exist"));
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @GetMapping("validation/{validationCode}")
    public void validate(@PathVariable String validationCode) {
        log.info("Got Validation code " + validationCode);
        authenticationService.checkEmailCode(validationCode);
    }

    @PostMapping("/validation/")
    public void create(@NotNull @RequestBody String uuid) {
        log.info("sending mail for " + uuid);
        UUID userId = UUID.fromString(uuid);
        authenticationService.createEmailValidationCodeForUserId(userId);
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
}