package com.mytruenorthproject.campsite.service.impl;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mytruenorthproject.campsite.model.Campsite;
import com.mytruenorthproject.campsite.model.Slot;
import com.mytruenorthproject.campsite.repository.CampsiteRepository;
import com.mytruenorthproject.campsite.repository.SlotRepository;
import com.mytruenorthproject.campsite.service.base.SlotsDataLoaderService;
import com.mytruenorthproject.campsite.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class SlotsDataLoaderServiceImpl implements SlotsDataLoaderService {

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    CampsiteRepository campsiteRepository;

    private static final String SLOTS_URL = "https://s3-sa-east-1.amazonaws.com/true-north-campsite-data/slots.json";
    private static final String CAMPSITE_URL = "https://s3-sa-east-1.amazonaws.com/true-north-campsite-data/campsite.json";

    @Override
    public void loadCampsiteDataForDemo() {
        String jsonResponse = HttpUtil.performGet(CAMPSITE_URL);
        JsonParser parser = new JsonParser();
        JsonElement campsiteJson =  parser.parse(jsonResponse);

        Gson gson = new Gson();
        Campsite campsite = gson.fromJson(campsiteJson, Campsite.class);
        campsiteRepository.save(campsite);
    }

    @Override
    public void loadSlotsDataForDemo() {
        String jsonResponse = HttpUtil.performGet(SLOTS_URL);
        JsonParser parser = new JsonParser();
        JsonElement slotsJson =  parser.parse(jsonResponse);
        Gson gson = new Gson();
        List<Slot> slots = Arrays.asList(gson.fromJson(slotsJson, Slot[].class));
        slotRepository.saveAll(slots);
    }

}
