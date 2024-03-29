package net.dancier.dancer.authentication;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.dto.SendLinkDto;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.dto.SetEmailValidationDto;
import net.dancier.dancer.authentication.dto.WhoAmIDto;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.service.AuthenticationService;
import net.dancier.dancer.authentication.service.CaptchaService;
import net.dancier.dancer.core.controller.payload.ApiResponse;
import net.dancier.dancer.core.controller.payload.JwtAuthenticationResponse;
import net.dancier.dancer.core.controller.payload.LoginRequestDto;
import net.dancier.dancer.core.exception.ApplicationException;
import net.dancier.dancer.core.exception.BusinessException;
import net.dancier.dancer.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.dancier.dancer.authentication.Constants.*;

@RestController
@RequestMapping(value = "/authentication", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class AuthenticationController {

    private static Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    private final CaptchaService captchaService;

    @GetMapping("/whoami")
    public ResponseEntity<?> whoami() {
        WhoAmIDto.WhoAmIDtoBuilder builder = new WhoAmIDto.WhoAmIDtoBuilder();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        authentication.getAuthorities().stream().forEach( a -> builder.addRole(a.getAuthority()));
        Object principalObject = authentication.getPrincipal();
        if (principalObject!=null && principalObject instanceof AuthenticatedUser) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) principalObject;
            builder.withEmailAddress(authenticatedUser.getUsername());
        }
        return ResponseEntity.ok(builder.build());
    }
    @Secured({ROLE_HUMAN})
    @PostMapping("/registrations")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        log.info("About to register user: " + registerRequest);
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
                .build();
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
        User user = authenticationService.getUser(authenticatedUser.getUserId());
        if (!user.isEmailValidated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "You have to validate the email."));
        }
        String jwt = authenticationService.generateJwtToken(authentication);
        ResponseCookie cookie = authenticationService.generateCookie(jwt);
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/loginAsHuman")
    public ResponseEntity<?> loginAsHuman(@RequestHeader(required = false, name = "X-Captcha-Token") String token,
                                          HttpServletResponse httpServletResponse) {
        log.info("Log in as human");
        ResponseCookie cookie = null;
        String jwt = null;
        try {
           captchaService.verifyToken(token);
           jwt = authenticationService.generateJwtToken("HUMAN");
           cookie = authenticationService
                   .generateCookie(jwt);
           httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        } catch (CaptchaException captchaException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse(false, "Not authorized as a human: " + captchaException.getMessage())
            );
        }
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse httpServletResponse) {
        ResponseCookie cookie = authenticationService.generateClearingCookie();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }

    @Secured(ROLE_HUMAN)
    @PostMapping("/email-validations")
    public ResponseEntity createEmailValidationCode(@NotNull @RequestBody SendLinkDto sendLinkDto) {
        log.info("sending mail for " + sendLinkDto.getEmail());
        authenticationService.createEmailValidationCode(sendLinkDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email-validations/{validationCode}")
    public ResponseEntity validateEmail(@PathVariable String validationCode, HttpServletResponse httpServletResponse) {
        User validatedUser = authenticationService.checkEmailValidationCode(validationCode);
        ResponseCookie cookie = authenticationService
                .generateCookie(authenticationService.generateJwtToken(validatedUser.getId().toString()));
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new ApiResponse(true, "Validated and logged in"));
    }

    @Secured(ROLE_ADMIN)
    @PutMapping("/email-validations")
    public ResponseEntity setEmailValidation(@NotNull @Valid @RequestBody SetEmailValidationDto setEmailValidationDto) {
        authenticationService.valideEmailByEmail(setEmailValidationDto.getEmailAddress());
        return ResponseEntity.ok().build();
    }

    @Secured(ROLE_HUMAN)
    @PostMapping("/password-changes")
    public ResponseEntity createPasswortResetCode(@RequestBody SendLinkDto sendLinkDto) {
        Optional<String> optionalCode =  authenticationService.createPasswordResetCode(sendLinkDto.getEmail().trim());
        if (optionalCode.isPresent()) {
            authenticationService.sendChangePasswordMail(sendLinkDto.getEmail().trim(), optionalCode.get());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password-changes/{validationCode}")
    public ResponseEntity changePassword(@PathVariable String validationCode,
                                         @RequestBody Map<String, String> newPasswortRequest) {
        String newPasswort = newPasswortRequest.get("password");
        authenticationService.checkPasswortCodeRequestAndCreateNew(validationCode, newPasswort);
        return ResponseEntity.ok().build();
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
    @ExceptionHandler(ApplicationException.class)
    public String handle(ApplicationException ae) {
        return ae.getLocalizedMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public String handle(BusinessException be) {
        return be.getLocalizedMessage();
    }

}
