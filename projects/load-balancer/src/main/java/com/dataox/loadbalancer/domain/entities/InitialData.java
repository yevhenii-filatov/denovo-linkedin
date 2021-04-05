package com.dataox.loadbalancer.domain.entities;

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
@Table(name = "initial_data")
@ToString(exclude = "searchResults")
public class InitialData {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "denovo_id")
    private Long denovoId;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "firm_name")
    private String firmName;

    @NotNull
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
