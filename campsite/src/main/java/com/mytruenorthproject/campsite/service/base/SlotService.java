package com.mytruenorthproject.campsite.service.base;

import com.mytruenorthproject.campsite.model.Slot;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface SlotService {

    Slot getById(Long id);
    List<Slot> getByDate(LocalDate date);
    List<LocalDate> getDatesAvailableByDateRange(LocalDate startDate, LocalDate endDate);
    Set<Slot> getByDateRangeAvailableAndCampsite(LocalDate startDate, LocalDate endDate, long id);
    Set<Slot> addSlotsToCampsite(Slot[] slots, Long id);
}
