package com.dataox.loadbalancer.domain.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Dmitriy Lysko
 * @since 23/03/2021
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "not_reusable_profile")
@NoArgsConstructor
public class LinkedinNotReusableProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Column(name = "error_description")
    String errorDescription;

    @NotNull
    @OneToOne
    @JoinColumn(name = "search_result_id", referencedColumnName = "id")
    SearchResult searchResult;

    @NotNull
    @Column(name = "scrape_licenses")
    boolean scrapeLicenses;

    @NotNull
    @Column(name = "scrape_volunteer")
    boolean scrapeVolunteer;

    @NotNull
    @Column(name = "scrape_interests")
    boolean scrapeInterests;

    @NotNull
    @Column(name = "scrape_recommendations")
    boolean scrapeRecommendations;

    @NotNull
    @Column(name = "scrape_accomplishments")
    boolean scrapeAccomplishments;

    @NotNull
    @Column(name = "scrape_skills")
    boolean scrapeSkills;

    @NotNull
    @Column(name = "scrape_activities")
    boolean scrapeActivities;
}
