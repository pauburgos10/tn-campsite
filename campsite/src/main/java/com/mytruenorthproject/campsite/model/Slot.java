package com.mytruenorthproject.campsite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mytruenorthproject.campsite.utils.SlotStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "slot")
@Data
@Builder
@EqualsAndHashCode(exclude={"campsite","reservation"})
@ToString(exclude = {"campsite", "reservation"})
public class Slot implements Serializable {

    //private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @NonNull
    @JsonIgnore
    @JoinColumn(name="campsiteId", nullable = false)
    private Campsite campsite;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "slots")
    private Set<Reservation> reservations;

    @Enumerated
    @Column(name = "status", nullable = false, columnDefinition = "int default 0")
    private SlotStatus status = SlotStatus.AVAILABLE;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
