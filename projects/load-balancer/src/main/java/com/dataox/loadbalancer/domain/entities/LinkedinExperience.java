package com.dataox.loadbalancer.domain.entities;

import com.dataox.loadbalancer.domain.types.LinkedinJobType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "linkedin_experience")
@NoArgsConstructor
public class LinkedinExperience {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private LinkedinJobType linkedinInterestType;

    @NotBlank
    @Column(name = "company_name")
    private String companyName;

    @NotBlank
    @Column(name = "company_profile_url")
    private String companyProfileUrl;

    @NotBlank
    @Column(name = "position")
    private String position;

    @NotBlank
    @Column(name = "date_started")
    private String dateStarted;

    @NotNull
    @Column(name = "date_started_timestamp")
    private LocalDate dateStartedTimestamp;

    @NotBlank
    @Column(name = "date_finished")
    private String dateFinished;

    @NotNull
    @Column(name = "date_finished_timestamp")
    private LocalDate dateFinishedTimestamp;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "total_duration")
    private String totalDuration;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;
}
