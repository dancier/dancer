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

    }
}
