package com.mytruenorthproject.campsite.service.impl;


import com.google.gson.*;
import com.mytruenorthproject.campsite.model.Campsite;
import com.mytruenorthproject.campsite.model.Slot;
import com.mytruenorthproject.campsite.repository.CampsiteRepository;
import com.mytruenorthproject.campsite.repository.SlotRepository;
import com.mytruenorthproject.campsite.service.base.SlotsDataLoaderService;
import com.mytruenorthproject.campsite.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDate;
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
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
        @Override
        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
        }
    }).create();

    @Override
    public void loadCampsiteDataForDemo() {
        String jsonResponse = HttpUtil.performGet(CAMPSITE_URL);
        JsonParser parser = new JsonParser();
        JsonElement campsiteJson =  parser.parse(jsonResponse);

        Campsite campsite = gson.fromJson(campsiteJson, Campsite.class);
        campsiteRepository.save(campsite);
    }

    @Override
    public void loadSlotsDataForDemo() {
        String jsonResponse = HttpUtil.performGet(SLOTS_URL);
        JsonParser parser = new JsonParser();
        JsonElement slotsJson =  parser.parse(jsonResponse);

        List<Slot> slots = Arrays.asList(gson.fromJson(slotsJson, Slot[].class));
        slotRepository.saveAll(slots);
    }

}
