package net.dancier.dancer.dancers;

import net.dancier.dancer.chat.dto.DancerDto;
import net.dancier.dancer.chat.dto.DancerIdsDto;
import net.dancier.dancer.core.dto.PublicProfileDto;
import net.dancier.dancer.core.exception.NotFoundException;
import net.dancier.dancer.core.model.Dancer;
import net.dancier.dancer.core.model.Gender;
import net.dancier.dancer.security.AuthenticatedUser;
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

    public List<PublicProfileDto> getDancersList(AuthenticatedUser authenticatedUser, Gender gender, int range) {

        Dancer dancer = loadByUserId(authenticatedUser.getUserId());
        Double longitudeRange = (double)range/112;
        Double latitudeRange = range/75.78;
        double upperLatitude = dancer.getLatitude() + latitudeRange;
        double lowerLatitude = dancer.getLatitude() - latitudeRange;
        double upperLongitude = dancer.getLongitude() + longitudeRange;
        double lowerLongitude = dancer.getLongitude() - longitudeRange;

        List<Dancer> resultList = dancerRepository.findFirst500ByGenderAndLongitudeBetweenAndLatitudeBetween(
            gender, lowerLongitude, upperLongitude, lowerLatitude, upperLatitude);

        return resultList.stream()
                .map(PublicProfileDto::of)
                .filter(d -> d.getId() != dancer.getId())
                .toList();
    }
}
