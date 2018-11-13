package com.mytruenorthproject.campsite.repository;

import com.mytruenorthproject.campsite.model.Slot;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends CrudRepository<Slot, Long> {

    List<Slot> findByDate(LocalDate arrivalDate);
}
