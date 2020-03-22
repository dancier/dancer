package de.frubumi.dance.dancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final DancerRepository dancerRepository;
    private final DancerUserRepository dancerUserRepository;

    @Autowired
    public DatabaseLoader(DancerRepository dancerRepository, DancerUserRepository dancerUserRepository) {
        this.dancerRepository = dancerRepository;
        this.dancerUserRepository = dancerUserRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.dancerUserRepository.save(new DancerUser("Marc", "Gorzala", "ROLE_MANAGER"));
        this.dancerUserRepository.save(new DancerUser("Jan", "Stroppel", "ROLE_MANAGER"));
        this.dancerUserRepository.save(new DancerUser("Xiaofei", "Shi", "ROLE_DANCER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Marc", "doesn't matter",
                        AuthorityUtils.createAuthorityList("ROLE_MANAGER")));
        this.dancerRepository.save(
                new Dancer("Marc", "Gorzala", "marc", "Tango"));

        this.dancerRepository.save(
                new Dancer("Xiaofei", "Shi", "xiaofei", "Tango"));

        this.dancerRepository.save(
                new Dancer("Jan", "Stroppel", "jan", "Tango"));

        this.dancerRepository.save(
                new Dancer("Bernd", "Brot", "Kasten", "Tango and Salsa")
        );
        this.dancerRepository.save(
                new Dancer("Nina", "H", "Nina", "Salsa")
        );
        this.dancerRepository.save(
                new Dancer("Julius", "H", "julsen", "keiner")
        );
        this.dancerRepository.save(
                new Dancer("Karsten", "Schöning", "Nussi", "keiner")
        );
        this.dancerRepository.save(
                new Dancer("Bastian", "S", "Uwe", "keiner")
        );
        this.dancerRepository.save(
                new Dancer("Christian", "D", "Hecht", "keiner")
        );
        SecurityContextHolder.clearContext();
    }
}
