package net.dancier.dancer.authentication.repository;

import net.dancier.dancer.authentication.model.Role;
import net.dancier.dancer.authentication.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleName roleName);
}
