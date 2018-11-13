package com.mytruenorthproject.campsite.repository;

import com.mytruenorthproject.campsite.model.Reservation;
import org.springframework.data.repository.CrudRepository;


public interface ReservationRepository extends CrudRepository<Reservation, Long> {


}
