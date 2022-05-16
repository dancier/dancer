package net.dancier.dancer.school;

import lombok.RequiredArgsConstructor;
import net.dancier.dancer.core.model.Country;
import net.dancier.dancer.core.model.Dance;
import net.dancier.dancer.core.model.Dancer;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    public List<School> getSchoolsForDancer(Dancer dancer) {
        return getStubbedSchools();
    }

    public void createSchool(UUID userid, School school) {
        Optional<School> optionalSchool = schoolRepository.findByUserId(userid);

    }
    private List<School> getStubbedSchools() {
        List<School> schools = new ArrayList<>();
        schools.add(getStubbedSchool());
        return schools;
    }

    private School getStubbedSchool() {
        School school = new School();
        school.setName("Piedra");
        school.setCity("Wuppertal");
        school.setUrl("http://tangoarte.de/");
        school.setCountry(Country.GER);
        Set<Dance> supportedDances = Set.of(new Dance(null, "Tango"), new Dance(null, "Salsa"));
        school.setSupportedDances(supportedDances);
        return school;
    }
}
