package fr.eql.ai115.boxing.club.repository;

import fr.eql.ai115.boxing.club.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberDao extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findMemberById(Long id);

    Boolean existsByEmail(String email);

    @Query("SELECT COUNT(r) > 0 FROM Member m JOIN m.roles r WHERE m.email = :email AND r.name = :roleName")
    boolean hasRole(@Param("email") String email, @Param("roleName") String roleName);
}
