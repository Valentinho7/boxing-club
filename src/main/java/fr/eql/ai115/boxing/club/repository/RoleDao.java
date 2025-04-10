package fr.eql.ai115.boxing.club.repository;

import fr.eql.ai115.boxing.club.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
