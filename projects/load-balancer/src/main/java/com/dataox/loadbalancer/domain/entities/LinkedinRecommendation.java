package com.dataox.loadbalancer.domain.entities;

import com.dataox.loadbalancer.domain.types.LinkedinRecommendationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_recommendation")
@NoArgsConstructor
public class LinkedinRecommendation {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LinkedinRecommendationType linkedinRecommendationType;

    @NotBlank
    @Column(name = "person_full_name", columnDefinition = "TEXT")
    private String personFullName;

    @NotBlank
    @Column(name = "person_profile_url", columnDefinition = "TEXT")
    private String personProfileUrl;

    @NotBlank
    @Column(name = "person_headline", columnDefinition = "TEXT")
    private String personHeadline;

    @NotBlank
    @Column(name = "person_extra_info", columnDefinition = "TEXT")
    private String personExtraInfo;

    @NotBlank
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;

    @Override
    public String toString() {
        return "LinkedinRecommendation{" +
                "id=" + id +
                ", updatedAt=" + updatedAt +
                ", itemSource='" + itemSource + '\'' +
                ", linkedinRecommendationType=" + linkedinRecommendationType +
                ", personFullName='" + personFullName + '\'' +
                ", personProfileUrl='" + personProfileUrl + '\'' +
                ", personHeadline='" + personHeadline + '\'' +
                ", personExtraInfo='" + personExtraInfo + '\'' +
                ", description='" + description + '\'' +
                ", linkedinProfile_id=" + linkedinProfile.getId() +
                '}';
    }
}
