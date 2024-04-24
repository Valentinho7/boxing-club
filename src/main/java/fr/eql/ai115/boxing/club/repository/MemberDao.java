package fr.eql.ai115.boxing.club.repository;

import fr.eql.ai115.boxing.club.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberDao extends JpaRepository<Member, Long> {

    Member findByEmail(String email);
}
