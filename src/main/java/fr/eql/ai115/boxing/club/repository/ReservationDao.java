package fr.eql.ai115.boxing.club.repository;

import fr.eql.ai115.boxing.club.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDao extends JpaRepository<Reservation, Long> {

    public Reservation findReservationById(Long id);

    public void deleteReservationById(Long id);
}
