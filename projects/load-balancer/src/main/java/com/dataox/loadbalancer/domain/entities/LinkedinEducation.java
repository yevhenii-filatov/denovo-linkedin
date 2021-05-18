package com.dataox.loadbalancer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_education")
@NoArgsConstructor
public class LinkedinEducation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotBlank
    @Column(name = "item_source", columnDefinition = "TEXT")
    private String itemSource;

    @NotBlank
    @Column(name = "institution_name", columnDefinition = "TEXT")
    private String institutionName;

    @Column(name = "institution_profile_url", columnDefinition = "TEXT")
    private String institutionProfileUrl;

    @Column(name = "degree", columnDefinition = "TEXT")
    private String degree;

    @Column(name = "field_of_study", columnDefinition = "TEXT")
    private String fieldOfStudy;

    @Column(name = "grade", columnDefinition = "TEXT")
    private String grade;

    @Column(name = "started_year", columnDefinition = "TEXT")
    private String startedYear;

    @Column(name = "finished_year", columnDefinition = "TEXT")
    private String finishedYear;

    @Column(name = "activities_and_societies", columnDefinition = "TEXT")
    private String activitiesAndSocieties;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;
}
