package com.huellapositiva.infrastructure.orm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "propolsal")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Propolsal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @JoinColumn(name = "organization_id")
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Organization organization;

    @JoinColumn(name = "location_id")
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Location location;

    @Column(name = "required_days", nullable = false)
    private String requiredDays;

    @Column(name = "min_age", nullable = false)
    private Integer minimumAge;

    @Column(name = "max_age", nullable = false)
    private Integer maximumAge;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "published", nullable = false)
    private Boolean published;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "volunteer_propolsals",
            joinColumns = {@JoinColumn(name = "propolsal_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "volunteer_id", referencedColumnName = "id")}
    )
    private Set<Volunteer> inscribedVolunteers;
}
