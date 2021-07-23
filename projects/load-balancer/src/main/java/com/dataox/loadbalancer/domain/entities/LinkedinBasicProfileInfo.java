package com.dataox.loadbalancer.domain.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_basic_profile_info")
@NoArgsConstructor
public class LinkedinBasicProfileInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

    //    @NotBlank
    @Column(name = "header_section_source", columnDefinition = "TEXT")
    private String headerSectionSource;

    @Column(name = "about_section_source", columnDefinition = "TEXT")
    private String aboutSectionSource;

    //    @NotBlank
    @Column(name = "full_name", columnDefinition = "TEXT")
    private String fullName;

    //    @NotBlank
    @Column(name = "number_of_connections", columnDefinition = "TEXT")
    private String numberOfConnections;

    //    @NotEmpty
    @Column(name = "location", columnDefinition = "TEXT")
    private String location;

    @Column(name = "cached_image_url", columnDefinition = "TEXT")
    private String cachedImageUrl;

    @Column(name = "about", columnDefinition = "TEXT")
    private String about;

    //    @NotNull
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;

    @Override
    public String toString() {
        return "LinkedinBasicProfileInfo{" +
                "id=" + id +
                ", updatedAt=" + updatedAt +
                ", headerSectionSource='" + headerSectionSource + '\'' +
                ", aboutSectionSource='" + aboutSectionSource + '\'' +
                ", fullName='" + fullName + '\'' +
                ", numberOfConnections='" + numberOfConnections + '\'' +
                ", location='" + location + '\'' +
                ", cachedImageUrl='" + cachedImageUrl + '\'' +
                ", about='" + about + '\'' +
                ", linkedinProfile_id=" + linkedinProfile.getId() +
                '}';
    }
}
