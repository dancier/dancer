package de.frubumi.dance.dancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final DancerRepository dancerRepository;

    @Autowired
    public DatabaseLoader(DancerRepository dancerRepository) {
        this.dancerRepository = dancerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.dancerRepository.save(
                new Dancer("Marc",
                        "Gorzala",
                        "recoby",
                        "Tango"));
    }
}
