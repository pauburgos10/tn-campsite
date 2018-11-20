package com.mytruenorthproject.campsite.service.impl;

import com.mytruenorthproject.campsite.model.Campsite;
import com.mytruenorthproject.campsite.repository.CampsiteRepository;
import com.mytruenorthproject.campsite.service.base.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

@Service
public class CampsiteServiceImpl implements CampsiteService {

    @Autowired
    CampsiteRepository campsiteRepository;

    @Override
    public Campsite createCampsite(String name) {
        Campsite campsite = Campsite.builder().name(name).build();
        try {
            return campsiteRepository.save(campsite);
        } catch (Exception e){
            throw new RuntimeException("You have entered an existing campsite name");
        }
    }
}
