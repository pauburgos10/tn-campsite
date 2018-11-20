package com.mytruenorthproject.campsite.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.mytruenorthproject.campsite.model.Campsite;
import com.mytruenorthproject.campsite.model.Slot;
import com.mytruenorthproject.campsite.repository.CampsiteRepository;
import com.mytruenorthproject.campsite.repository.SlotRepository;
import com.mytruenorthproject.campsite.service.base.SlotService;
import com.mytruenorthproject.campsite.utils.SlotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SlotServiceImpl implements SlotService {
    @Autowired
    SlotRepository slotRepository;

    @Autowired
    CampsiteRepository campsiteRepository;


    @Override
    @Transactional(readOnly = true)
    public Slot getById(Long id) {
        Optional<Slot> slot = slotRepository.findById(id);

        if (!slot.isPresent()){
            throw new RuntimeException("Reservation not found");
        }

        return slot.get();
    }

    @Override
    public List<Slot> getByDate(LocalDate date) {
        Optional<List<Slot>> slots = slotRepository.findByDate(date);
        if (!slots.isPresent()){
            throw new RuntimeException("Slot not available");
        }
        return slots.get();
    }

    @Override
    public List<LocalDate> getDatesAvailableByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null){
            startDate = LocalDate.now().plusDays(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusDays(31);
        }

        LocalDate now = LocalDate.now();
        Preconditions.checkArgument(startDate.isAfter(now), "Start date must be a future date");
        Preconditions.checkArgument(endDate.isAfter(now), "End date must be a future date");
        Preconditions.checkArgument(startDate.isEqual(endDate) || startDate.isBefore(endDate),
                "End date must be equal or greater than start date");

        List<LocalDate> dates = slotRepository.findDateByDateBetweenAndStatus(startDate, endDate, SlotStatus.AVAILABLE);
        return dates;
    }



    @Override
    public Set<Slot> getByDateRangeAvailableAndCampsite(LocalDate startDate, LocalDate endDate, long id) {

        Optional<Campsite> campsite = campsiteRepository.findById(id);
        if (!campsite.isPresent()){
            throw new RuntimeException("Campsite not found");
        }

        Set<Slot> slots = slotRepository.findByDateBetweenAndStatusAndCampsite(startDate,endDate,SlotStatus.AVAILABLE, campsite.get());
        return slots;
    }

    @Override
    public Set<Slot> addSlotsToCampsite(Slot[] slots, Long id) {
        Optional<Campsite> campsite = campsiteRepository.findById(id);
        if (!campsite.isPresent()){
            throw new RuntimeException("Campsite not found");
        }

        Set<Slot> slotSet = Sets.newHashSet(slots);
        slotSet.stream().forEach(slot -> slot.setCampsite(campsite.get()));
        slotRepository.saveAll(slotSet);
        return slotSet;
    }


}
