package net.dancier.dancer.core;

import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DancerService {

    @Autowired
    DancerRepository dancerRepository;

    public DancerDto getDancerByUserId(UUID userId) {
        return ModelMapper.dancerToDancerDto(dancerRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Dancer tmp = new Dancer();
                    tmp.setUserId(userId
                    );
                    return tmp;
                }));
    }

    public void save(Dancer dancer) {
        this.dancerRepository.save(dancer);
    }

}
