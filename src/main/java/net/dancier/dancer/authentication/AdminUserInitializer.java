package net.dancier.dancer.authentication;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.authentication.dto.RegisterRequestDto;
import net.dancier.dancer.authentication.model.Role;
import net.dancier.dancer.authentication.model.RoleName;
import net.dancier.dancer.authentication.model.User;
import net.dancier.dancer.authentication.repository.RoleRepository;
import net.dancier.dancer.authentication.repository.UserRepository;
import net.dancier.dancer.authentication.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Profile("!it")
@Component
@RequiredArgsConstructor
public class AdminUserInitializer {

    private static Logger log = LoggerFactory.getLogger(AdminUserInitializer.class);

    @Value("${app.admin.email}")
    String email;

    @Value("${app.admin.pass}")
    String pass;

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Transactional
    @PostConstruct
    public void init() {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            log.info(String.format("User: %s already exists. Not creating admin user.", email));
        } else {
            log.info("Creating Admin User:" + email);
            RegisterRequestDto registerRequestDto = new RegisterRequestDto();
            registerRequestDto.setAcceptTermsAndConditions(true);
            registerRequestDto.setEmail(email);
            registerRequestDto.setPassword(pass);
            authenticationService.registerUser(registerRequestDto);

            User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException());
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new IllegalStateException() );
            Collection roles = user.getRoles();
            roles.add(adminRole);
            user.setRoles(roles);
            user.setEmailValidated(true);
            userRepository.save(user);
        }
    }

}
