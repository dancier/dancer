package de.frubumi.dance.dancer;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_DANCER')")
public interface DancerRepository extends PagingAndSortingRepository<Dancer, Long> {
    @Override
    @PreAuthorize("#dancer?.firstName == authentication?.name or hasRole('ROLE_MANAGER')")
    Dancer save(@Param("dancer") Dancer dancer);

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#dancer?.firstName == authentication?.name or hasRole('ROLE_MANAGER')")
    void delete(@Param("dancer") Dancer dancer);

}
