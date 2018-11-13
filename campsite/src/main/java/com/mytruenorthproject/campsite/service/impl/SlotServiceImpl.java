package com.mytruenorthproject.campsite.service.impl;

import com.mytruenorthproject.campsite.model.Slot;
import com.mytruenorthproject.campsite.repository.SlotRepository;
import com.mytruenorthproject.campsite.service.base.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SlotServiceImpl implements SlotService {
    @Autowired
    SlotRepository slotRepository;


    @Override
    public Slot getById(Long id) {
        Optional<Slot> slot = slotRepository.findById(id);

        if (!slot.isPresent()){
            throw new RuntimeException("Reservation not found");
        }

        return slot.get();
    }
}
