package com.mytruenorthproject.campsite.service.base;

import com.mytruenorthproject.campsite.model.Reservation;

public interface ReservationService {

    Reservation findById(Long id);
    Reservation createReservation(Reservation reservation, long campsiteId);
    Reservation updateReservation(Reservation reservation);
    void cancelReservation(long id);
}
