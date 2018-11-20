package com.mytruenorthproject.campsite.service.impl;

import com.google.common.base.Preconditions;
import com.mytruenorthproject.campsite.model.Slot;
import com.mytruenorthproject.campsite.repository.ReservationRepository;
import com.mytruenorthproject.campsite.model.Reservation;
import com.mytruenorthproject.campsite.repository.SlotRepository;
import com.mytruenorthproject.campsite.service.base.ReservationService;
import com.mytruenorthproject.campsite.utils.ReservationStatus;
import com.mytruenorthproject.campsite.utils.SlotStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;


@Service
public class ReservationServiceImpl implements ReservationService {

    public static final int ADJUST_DEPARTURE_DATE = 1;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    SlotServiceImpl slotService;

    @Autowired
    SlotRepository slotRepository;

    @Value("${campsite.max.consecutive.booking.days}")
    private int maxConsecutiveBookingDays;

    @Value("${campsite.min.days.before.reservation}")
    private int minDaysBeforeReservation;

    @Value("${campsite.max.days.before.reservation}")
    private int maxDaysBeforeReservation;



    @Override
    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
       Optional<Reservation> reservation = reservationRepository.findById(id);

        if (!reservation.isPresent()){
            throw new RuntimeException("Reservation not found");
        }

        return reservation.get();
    }

    @Override
    @Transactional
    @Retryable(include = CannotAcquireLockException.class,
            maxAttempts = 2, backoff=@Backoff(delay = 500, maxDelay = 1000))
    public Reservation createReservation(Reservation reservation, long campsiteId) {
        if (!reservation.isNewRecord()) {
            throw new RuntimeException("New booking must not have id");
        }
        Set<Slot> slots = validateReservationParameters(reservation, campsiteId);
        slots.stream().forEach(slot -> slot.setStatus(SlotStatus.RESERVED));
        reservation.setSlots(slots);

        Reservation saved = reservationRepository.save(reservation);
        slotRepository.saveAll(slots);

        return saved;
    }

    private Set<Slot> validateReservationParameters(Reservation reservation, long campsiteId) {
        LocalDate now = LocalDate.now();
        Preconditions.checkArgument(reservation.getArrivalDate().isAfter(now),
                "Reservation must be a future date");
        Preconditions.checkArgument(reservation.getArrivalDate().isBefore(reservation.getDepartureDate()),
                "End date must be greater than start date");
        Preconditions.checkArgument(now.plusDays(maxDaysBeforeReservation).isAfter(reservation.getArrivalDate()) ||
                        now.plusMonths(1).isEqual(reservation.getArrivalDate()),
                "Reservation can not be made more than one month in advanced");
        Preconditions.checkArgument(reservation.getArrivalDate().plusDays(maxConsecutiveBookingDays).isAfter(reservation.getDepartureDate().minusDays(1)),
                "Reservation can not be longer than 3 days");

        Set<Slot> slots = slotService.getByDateRangeAvailableAndCampsite(reservation.getArrivalDate(),
                reservation.getDepartureDate().minusDays(ADJUST_DEPARTURE_DATE), campsiteId );

        if (slots == null || slots.isEmpty() || slots.size() < ChronoUnit.DAYS.between(reservation.getArrivalDate(), reservation.getDepartureDate())){
            throw new RuntimeException("Dates Requested are no longer available");
        }

        return slots;
    }

    @Override
    @Transactional
    public Reservation updateReservation(Reservation reservationRequest) {
        Optional<Reservation> reservationPersisted = reservationRepository.findById(reservationRequest.getId());

        if (!reservationPersisted.isPresent()){
            throw new RuntimeException("Reservation not found");
        }

        Reservation reservation = reservationPersisted.get();

        Set<Slot> oldSlots = reservation.getSlots();
        oldSlots.stream().forEach(slot -> slot.setStatus(SlotStatus.AVAILABLE));
        //slotRepository.saveAll(oldSlots);

        Set<Slot> newSlots = validateReservationParameters(reservationRequest, reservation.getCampsiteId());
        newSlots.stream().forEach(slot -> slot.setStatus(SlotStatus.RESERVED));
        reservationRequest.setSlots(newSlots);
        Reservation saved = reservationRepository.save(reservationRequest);
        //slotRepository.saveAll(newSlots);
        return saved;
    }

    @Override
    @Transactional
    public void cancelReservation(long id) {
        Optional<Reservation> reservationPersisted = reservationRepository.findById(id);

        if (!reservationPersisted.isPresent()){
            throw new RuntimeException("Reservation not found");
        }

        Reservation reservation = reservationPersisted.get();
        reservation.setStatus(ReservationStatus.CANCELLED);
        Set<Slot> slots = reservation.getSlots();
        slots.stream().forEach(slot -> slot.setStatus(SlotStatus.AVAILABLE));
        reservationRepository.save(reservation);
    }

}
