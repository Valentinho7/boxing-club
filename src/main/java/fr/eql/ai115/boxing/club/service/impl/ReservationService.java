package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Reservation;
import fr.eql.ai115.boxing.club.repository.ReservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationDao reservationDao;

    public void saveReservation(Reservation reservation) {
        reservationDao.save(reservation);
    }

    public List<Reservation> findAllReservations() {
        return reservationDao.findAll();
    }

    public List<Reservation> findAllByMemberId(Long memberId) {
        return reservationDao.findAllByMemberId(memberId);
    }

    public Optional<Reservation> findById(Long id) {
        return reservationDao.findById(id);
    }

    public List<Reservation> findAllValidateReservations() {
        return reservationDao.findAllByIsValidateTrue();
    }

    public void validateReservation(Long reservationId) {
        Reservation reservation = reservationDao.findReservationById(reservationId);
        reservation.setValidate(true);
        reservation.setValidateDate(LocalDate.now());
        reservationDao.save(reservation);
    }
}
