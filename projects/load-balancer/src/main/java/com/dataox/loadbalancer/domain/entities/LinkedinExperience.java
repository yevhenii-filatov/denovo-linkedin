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

//    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

//    @NotBlank
    @Column(name = "item_source", columnDefinition = "TEXT")
    private String itemSource;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private LinkedinJobType linkedinJobType;

//    @NotBlank
    @Column(name = "company_name", columnDefinition = "TEXT")
    private String companyName;

//    @NotBlank
    @Column(name = "company_profile_url", columnDefinition = "TEXT")
    private String companyProfileUrl;

//    @NotBlank
    @Column(name = "position", columnDefinition = "TEXT")
    private String position;

    @Column(name = "date_started", columnDefinition = "TEXT")
    private String dateStarted;

    @Column(name = "date_started_timestamp")
    private LocalDate dateStartedTimestamp;

    @Column(name = "date_finished", columnDefinition = "TEXT")
    private String dateFinished;

    @Column(name = "date_finished_timestamp")
    private LocalDate dateFinishedTimestamp;

    @Column(name = "location", columnDefinition = "TEXT")
    private String location;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "total_duration", columnDefinition = "TEXT")
    private String totalDuration;

//    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;

    @Override
    public String toString() {
        return "LinkedinExperience{" +
                "id=" + id +
                ", updatedAt=" + updatedAt +
                ", itemSource='" + itemSource + '\'' +
                ", linkedinJobType=" + linkedinJobType +
                ", companyName='" + companyName + '\'' +
                ", companyProfileUrl='" + companyProfileUrl + '\'' +
                ", position='" + position + '\'' +
                ", dateStarted='" + dateStarted + '\'' +
                ", dateStartedTimestamp=" + dateStartedTimestamp +
                ", dateFinished='" + dateFinished + '\'' +
                ", dateFinishedTimestamp=" + dateFinishedTimestamp +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", totalDuration='" + totalDuration + '\'' +
                ", linkedinProfile_id=" + linkedinProfile.getId() +
                '}';
    }
}
