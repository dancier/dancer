package net.dancier.dancer.core;

import net.dancier.dancer.chat.dto.DancerDto;
import net.dancier.dancer.chat.dto.DancerIdsDto;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.core.model.Dancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class DancerService {

    @Autowired
    DancerRepository dancerRepository;

    public Dancer loadByUserId(UUID userId) {
        return dancerRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("No Profile found for given user."));
    }

    public List<Dancer> getAllDancer() {
        return dancerRepository.findAll();
    }

    public HashMap<UUID, DancerDto> getDancerMap(DancerIdsDto dancerIdsDto) {

        HashMap<UUID, DancerDto> dancers = new HashMap<>();

        dancerRepository.findAllById(dancerIdsDto.getDancerIds())
                .stream()
                .map(DancerDto::fromDancer)
                .forEach(dancerDto -> dancers.put(dancerDto.getId(), dancerDto));

        return dancers;
    }
}
