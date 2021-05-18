package com.dataox.loadbalancer.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "search_result")
public class SearchResult {

    public SearchResult(String url, int searchPosition, int searchStep, InitialData initialDataRecord) {
        this.url = url;
        this.searchPosition = searchPosition;
        this.searchStep = searchStep;
        this.initialDataRecord = initialDataRecord;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "collected_at")
    private Instant collectedAt;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @NotNull
    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "search_position")
    private int searchPosition;

    @NotNull
    @Column(name = "searchStep")
    private int searchStep;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "initial_data_record_id", nullable = false)
    private InitialData initialDataRecord;

    @OneToOne(mappedBy = "searchResult")
    private LinkedinProfile linkedinProfile;

    @PrePersist
    private void setCollectedAt() {
        this.collectedAt = Instant.now();
    }
}
