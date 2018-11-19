package com.mytruenorthproject.campsite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mytruenorthproject.campsite.utils.ReservationStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Reservation")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude="slots")
@ToString(exclude = {"slots"})
public class Reservation implements Serializable {

   //private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    /**
     * Holds value of property full name.
     */
    @NotEmpty
    @Size(max = 50)
    @Column(name = "name", nullable = false)
    private String name;

    @NotEmpty
    @Size(max = 50)
    @Column(name = "lastName", nullable = false)
    private String lastName;

    @NotNull
    @Future(message = "Booking start date must be a future date")
    @Column(name = "arrivalDate", nullable = false)
    private LocalDate arrivalDate;

    @NotNull
    @Future(message = "Booking start date must be a future date")
    @Column(name = "departureDate", nullable = false)
    private LocalDate departureDate;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "slot_reservation_pairs",
            joinColumns = { @JoinColumn(name = "reservation_id") },
            inverseJoinColumns = { @JoinColumn(name = "slot_id") })
    private Set<Slot> slots = new HashSet<>();

    @NotNull
    @Column(name = "status", nullable = false, columnDefinition = "varchar(32) default 'ACTIVE'")
    private ReservationStatus status = ReservationStatus.ACTIVE;

    @JsonIgnore
    public boolean isNewRecord() {
        return this.id == null;
    }

    @JsonIgnore
    public boolean isActive() {
        return this.status == ReservationStatus.ACTIVE;
    }

    @JsonIgnore
    public Long getCampsiteId() {
        if(!slots.isEmpty()){
            return this.slots.iterator().next().getCampsite().getId();
        }
        return null;
    }

    @JsonIgnore
    public Campsite getCampsite() {
        if(!slots.isEmpty()){
            return this.slots.iterator().next().getCampsite();
        }
        return null;
    }
}
