package com.mytruenorthproject.campsite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mytruenorthproject.campsite.utils.SlotStatus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "slot")
@Data
@Builder
public class Slot {

    //private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NonNull
    @JsonIgnore
    @JoinColumn(name="campsiteId")
    private Campsite campsite;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "slots")
    private Set<Reservation> reservations;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(32) default 'AVAILABLE'")
    private SlotStatus status = SlotStatus.AVAILABLE;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

}
