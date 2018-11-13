package com.mytruenorthproject.campsite.controller;


import com.mytruenorthproject.campsite.model.Reservation;
import com.mytruenorthproject.campsite.model.Slot;
import com.mytruenorthproject.campsite.service.base.ReservationService;
import com.mytruenorthproject.campsite.service.base.SlotService;
import com.mytruenorthproject.campsite.service.base.SlotsDataLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campsite")
public class CampsiteController {

    @Autowired
    ReservationService reservationService;

    @Autowired
    SlotsDataLoaderService slotsDataLoaderService;

    @Autowired
    SlotService slotService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Resource<Reservation>> getBooking(@PathVariable() long id) {

        Reservation booking = reservationService.findById(id);
        Resource<Reservation> resource = new Resource<>(booking);

        Link selfLink = ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(this.getClass()).getBooking(id)).withSelfRel();
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



    @PostMapping("/demo")
    public ResponseEntity<Void> loadDataForDemo() {
        slotsDataLoaderService.loadCampsiteDataForDemo();
        slotsDataLoaderService.loadSlotsDataForDemo();
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

}
