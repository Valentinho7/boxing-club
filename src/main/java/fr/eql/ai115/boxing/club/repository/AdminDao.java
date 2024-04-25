package fr.eql.ai115.boxing.club.repository;

import fr.eql.ai115.boxing.club.entity.Admin;
import fr.eql.ai115.boxing.club.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminDao extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);

    Boolean existsByEmail(String email);


    @Query("SELECT COUNT(r) > 0 FROM Admin a JOIN a.roles r WHERE a.email = :email AND r.name = :roleName")
    boolean hasRole(@Param("email") String email, @Param("roleName") String roleName);
}
