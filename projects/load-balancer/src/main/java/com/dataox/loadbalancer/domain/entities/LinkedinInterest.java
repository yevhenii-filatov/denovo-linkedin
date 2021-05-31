package com.dataox.loadbalancer.domain.entities;

import com.dataox.loadbalancer.domain.types.LinkedinInterestType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_interest")
@NoArgsConstructor
public class LinkedinInterest {

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
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

//    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LinkedinInterestType linkedinInterestType;

//    @NotBlank change in database to not null
    @Column(name = "profile_url", columnDefinition = "TEXT")
    private String profileUrl;

    @Column(name = "headline", columnDefinition = "TEXT")
    private String headline;

//    @NotBlank
    @Column(name = "number_of_followers", columnDefinition = "TEXT")
    private String numberOfFollowers;

//    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;

    @Override
    public String toString() {
        return "LinkedinInterest{" +
                "id=" + id +
                ", updatedAt=" + updatedAt +
                ", itemSource='" + itemSource + '\'' +
                ", name='" + name + '\'' +
                ", linkedinInterestType=" + linkedinInterestType +
                ", profileUrl='" + profileUrl + '\'' +
                ", headline='" + headline + '\'' +
                ", numberOfFollowers='" + numberOfFollowers + '\'' +
                ", linkedinProfile_id=" + linkedinProfile.getId() +
                '}';
    }
}
