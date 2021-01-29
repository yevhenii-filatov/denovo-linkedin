package com.dataox.loadbalancer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_volunteer_experience")
@NoArgsConstructor
public class LinkedinVolunteerExperience {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotBlank
    @Column(name = "item_source")
    private String itemSource;

    @NotBlank
    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_profile_url")
    private String companyProfileUrl;

    @NotBlank
    @Column(name = "position")
    private String position;

    @NotBlank
    @Column(name = "date_started")
    private String dateStarted;

    @NotBlank
    @Column(name = "date_finished")
    private String dateFinished;

    @NotBlank
    @Column(name = "total_duration")
    private String totalDuration;

    @Column(name = "field_of_activity")
    private String fieldOfActivity;

    @Column(name = "description")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;
}
