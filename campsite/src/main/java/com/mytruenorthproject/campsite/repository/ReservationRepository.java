package com.mytruenorthproject.campsite.repository;

import com.mytruenorthproject.campsite.model.Reservation;
import com.mytruenorthproject.campsite.model.Slot;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.Set;


public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Query("SELECT r.slots FROM Reservation r where r.id = :id")
    Set<Slot> findSlotsById(@Param("id") Long id);

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Reservation> findById(Long aLong);
}
