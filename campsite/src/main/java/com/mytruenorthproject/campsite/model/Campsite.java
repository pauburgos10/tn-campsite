package com.mytruenorthproject.campsite.model;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Campsite")
@Data
@Builder
@EqualsAndHashCode(exclude="slots")
@ToString(exclude = {"slots"})
public class Campsite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @UniqueElements
    @Column
    private String name;

    @OneToMany(cascade= CascadeType.ALL,mappedBy="campsite",fetch = FetchType.LAZY)
    private Set<Slot> slots;

}
