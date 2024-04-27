package fr.eql.ai115.boxing.club.service.impl;

import fr.eql.ai115.boxing.club.entity.Reservation;
import fr.eql.ai115.boxing.club.repository.ReservationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservationService {

    @Autowired
    private ReservationDao reservationDao;

    public Reservation saveReservation(Reservation reservation) {
        return reservationDao.save(reservation);
    }
}
