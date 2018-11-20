package com.mytruenorthproject.campsite.repository;

import com.mytruenorthproject.campsite.model.Campsite;
import com.mytruenorthproject.campsite.model.Reservation;
import com.mytruenorthproject.campsite.model.Slot;
import com.mytruenorthproject.campsite.utils.SlotStatus;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SlotRepository extends CrudRepository<Slot, Long> {

    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    List<LocalDate> findDateByDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, SlotStatus status);

    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<List<Slot>> findByDate(LocalDate date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Set<Slot> findByDateBetweenAndStatusAndCampsite(LocalDate startDate, LocalDate endDate, SlotStatus status, Campsite campsite);


}
