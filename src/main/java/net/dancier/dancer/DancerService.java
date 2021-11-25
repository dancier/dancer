package net.dancier.dancer;

import net.dancier.dancer.exception.AppException;
import net.dancier.dancer.model.Dancer;
import net.dancier.dancer.repository.DancerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DancerService {

    @Autowired
    DancerRepository dancerRepository;

    public Dancer getDancerById(UUID userId) {
        return dancerRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Dancer tmp = new Dancer();
                    tmp.setUserId(userId
                    );
                    return tmp;
                });
    }

    public void save(Dancer dancer) {
        this.dancerRepository.save(dancer);
    }

}
