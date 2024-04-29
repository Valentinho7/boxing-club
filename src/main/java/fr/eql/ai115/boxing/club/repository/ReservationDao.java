package fr.eql.ai115.boxing.club.repository;

import fr.eql.ai115.boxing.club.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationDao extends JpaRepository<Reservation, Long> {

    public Reservation findReservationById(Long id);

    public void deleteReservationById(Long id);

    @Query("SELECT r FROM Reservation r WHERE r.member.id = :memberId")
    public List<Reservation> findAllByMemberId(@Param("memberId") Long memberId);

    List<Reservation> findAllByIsValidateTrue();

}

