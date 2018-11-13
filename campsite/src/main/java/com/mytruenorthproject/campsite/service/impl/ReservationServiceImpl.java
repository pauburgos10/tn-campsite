package com.mytruenorthproject.campsite.service.impl;

import com.mytruenorthproject.campsite.repository.ReservationRepository;
import com.mytruenorthproject.campsite.model.Reservation;
import com.mytruenorthproject.campsite.service.base.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationRepository reservationRepository;


    @Override
    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (!reservation.isPresent()){
            throw new RuntimeException("Reservation not found");
        }

        return reservation.get();
    }
}
