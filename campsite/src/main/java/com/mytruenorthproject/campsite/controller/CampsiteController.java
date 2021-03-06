package com.mytruenorthproject.campsite.controller;


import com.google.common.collect.Sets;
import com.mytruenorthproject.campsite.model.Campsite;
import com.mytruenorthproject.campsite.model.Reservation;
import com.mytruenorthproject.campsite.model.Slot;
import com.mytruenorthproject.campsite.service.base.CampsiteService;
import com.mytruenorthproject.campsite.service.base.ReservationService;
import com.mytruenorthproject.campsite.service.base.SlotService;
import com.mytruenorthproject.campsite.service.base.SlotsDataLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/campsite")
public class CampsiteController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    SlotsDataLoaderService slotsDataLoaderService;

    @Autowired
    SlotService slotService;

    @Autowired
    CampsiteService campsiteService;

    @GetMapping(value = "reservation/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Resource<Reservation>> getReservation(@PathVariable() long id) {

        Reservation booking = reservationService.findById(id);
        Resource<Reservation> resource = new Resource<>(booking);

        Link selfLink = ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(this.getClass()).getReservation(id)).withSelfRel();
        resource.add(selfLink);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }


    @GetMapping(value = "slot/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Resource<Slot>> getSlot(@PathVariable() long id) {

        Slot slot = slotService.getById(id);
        Resource<Slot> resource = new Resource<>(slot);

        Link selfLink = ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(this.getClass()).getSlot(id)).withSelfRel();
        resource.add(selfLink);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping(value = "/availability", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Resources<LocalDate>> getSlotAvailability(@RequestParam(name = "start_date", required = false)
                                                      @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
                                                                     @RequestParam(name = "end_date", required = false)
                                                      @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {

        List<LocalDate> dates = slotService.getDatesAvailableByDateRange(startDate, endDate);
        Resources<LocalDate> resource = new Resources<LocalDate>(dates);

        Link selfLink = ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(this.getClass()).getSlotAvailability(startDate, endDate)).withSelfRel();
        resource.add(selfLink);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping (value = "/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Resource<Campsite>> createCampsite(@PathVariable() String name) {


        Campsite campsite = campsiteService.createCampsite(name);
        Resource<Campsite> resource = new Resource<>(campsite);

        Link selfLink = ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(this.getClass()).createCampsite(name)).withSelfRel();
        resource.add(selfLink);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(resource,headers, HttpStatus.OK);
    }

    @PostMapping(value = "/slots/{campsiteId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Resources<Slot>> addSlots(@PathVariable() long campsiteId, @RequestBody @Valid Slot[] slots) {


        Set<Slot> slotSet = slotService.addSlotsToCampsite(slots, campsiteId);
        Resources<Slot> resources = new Resources<>(slotSet);

        Link selfLink = ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(this.getClass()).addSlots(campsiteId, slots)).withSelfRel();
        resources.add(selfLink);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(resources,headers, HttpStatus.OK);
    }

    @PostMapping(value = "/reservation/{campsite_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Resource<Reservation>> createReservation(@PathVariable() long campsite_id,
                                                                   @RequestBody() @Valid Reservation reservation) {

        Reservation newReservation = reservationService.createReservation(reservation, campsite_id);
        Resource<Reservation> resource = new Resource<>(newReservation);

        Link selfLink = ControllerLinkBuilder
                .linkTo(this.getClass()).slash(newReservation.getId()).withSelfRel();
        resource.add(selfLink);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(resource, headers, HttpStatus.CREATED);
    }


    @PutMapping(value = "/reservation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Resource<Reservation>> updateReservation(@RequestBody @Valid Reservation reservation) {

        Reservation updatedReservation = reservationService.updateReservation(reservation);
        Resource<Reservation> resource = new Resource<>(updatedReservation);

        Link selfLink = ControllerLinkBuilder
                .linkTo(this.getClass()).slash(updatedReservation.getId()).withSelfRel();
        resource.add(selfLink);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @DeleteMapping(value = "reservation/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> cancelReservation(@PathVariable("id") long id) {
        reservationService.cancelReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/demo")
    public ResponseEntity<Void> loadDataForDemo() {
        slotsDataLoaderService.loadCampsiteDataForDemo();
        slotsDataLoaderService.loadSlotsDataForDemo();
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
