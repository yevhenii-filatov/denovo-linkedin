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

//    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

//    @NotBlank
    @Column(name = "item_source", columnDefinition = "TEXT")
    private String itemSource;

//    @NotBlank
    @Column(name = "company_name", columnDefinition = "TEXT")
    private String companyName;

    @Column(name = "company_profile_url", columnDefinition = "TEXT")
    private String companyProfileUrl;

//    @NotBlank
    @Column(name = "position", columnDefinition = "TEXT")
    private String position;

//    @NotBlank
    @Column(name = "date_started", columnDefinition = "TEXT")
    private String dateStarted;

//    @NotBlank
    @Column(name = "date_finished", columnDefinition = "TEXT")
    private String dateFinished;

//    @NotBlank
    @Column(name = "total_duration", columnDefinition = "TEXT")
    private String totalDuration;

    @Column(name = "field_of_activity", columnDefinition = "TEXT")
    private String fieldOfActivity;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

//    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;

    @Override
    public String toString() {
        return "LinkedinVolunteerExperience{" +
                "id=" + id +
                ", updatedAt=" + updatedAt +
                ", itemSource='" + itemSource + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyProfileUrl='" + companyProfileUrl + '\'' +
                ", position='" + position + '\'' +
                ", dateStarted='" + dateStarted + '\'' +
                ", dateFinished='" + dateFinished + '\'' +
                ", totalDuration='" + totalDuration + '\'' +
                ", fieldOfActivity='" + fieldOfActivity + '\'' +
                ", description='" + description + '\'' +
                ", linkedinProfile_id=" + linkedinProfile.getId() +
                '}';
    }
}
