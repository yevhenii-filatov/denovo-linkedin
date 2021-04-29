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

    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotBlank
    @Column(name = "item_source")
    private String itemSource;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LinkedinInterestType linkedinInterestType;

//    @NotBlank change in database to not null
    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "headline")
    private String headline;

    @NotBlank
    @Column(name = "number_of_followers")
    private String numberOfFollowers;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;
}
