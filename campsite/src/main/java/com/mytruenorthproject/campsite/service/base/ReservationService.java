package com.mytruenorthproject.campsite.service.base;

import com.mytruenorthproject.campsite.model.Reservation;

public interface ReservationService {

    Reservation findById(Long id);
}
