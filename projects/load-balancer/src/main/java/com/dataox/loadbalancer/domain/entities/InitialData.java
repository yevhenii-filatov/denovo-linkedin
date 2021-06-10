package com.dataox.loadbalancer.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "google_initial_data")
@ToString(exclude = "searchResults")
@JsonIgnoreProperties(value = {"searchMetadata", "searchResults"})
public class InitialData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "denovo_id")
    private Long denovoId;

    @Column(name = "updated_at")
    private Instant updatedAt;

//    @NotNull
    @Column(name = "first_name", columnDefinition = "TEXT")
    private String firstName;

//    @NotNull
    @Column(name = "middle_name", columnDefinition = "TEXT")
    private String middleName;

//    @NotNull
    @Column(name = "last_name", columnDefinition = "TEXT")
    private String lastName;

//    @NotNull
    @Column(name = "firm_name", columnDefinition = "TEXT")
    private String firmName;

    @Column(name = "linkedin_url", length = 1000)
    private String linkedinUrl;

//    @NotNull
    @Column(name = "searched")
    private Boolean searched;

    @Column(name = "no_results")
    private Boolean noResults;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "search_metadata_id", referencedColumnName = "id")
    private SearchMetadata searchMetadata;

    @OneToMany(mappedBy = "initialDataRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SearchResult> searchResults = new ArrayList<>();

    @PreUpdate
    void setUpdatedAd() {
        this.updatedAt = Instant.now();
    }
}
