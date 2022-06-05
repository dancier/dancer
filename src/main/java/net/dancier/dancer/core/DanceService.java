package net.dancier.dancer.core;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.model.Dance;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DanceService {

    private final DanceRepository danceRepository;
    public Set<Dance> getAllDances() {
        return new HashSet<>(this.danceRepository.findAll());
    }

    public void saveAll(Set<Dance> dances) {
        danceRepository.saveAll(dances);
    }

}
