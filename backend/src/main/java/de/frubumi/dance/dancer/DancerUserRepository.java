package de.frubumi.dance.dancer;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface DancerUserRepository extends Repository<DancerUser, Long> {

    DancerUser save(DancerUser user);

    DancerUser findByName(String name);

}
