package com.dataox.loadbalancer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

/**
 * @author Dmitriy Lysko
 * @since 16/03/2021
 */
@Data
@Entity
@Table(name = "scraping_batch")
@NoArgsConstructor
public class ScrapingBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at",updatable = false)
    private Instant createdAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @OneToMany(mappedBy = "scrapingBatch", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SearchResult> searchResults;

    @Column(name = "finished")
    private boolean finished;

    @PrePersist
    private void setTime() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    private void seFinishedAt() {
        this.finishedAt = Instant.now();
    }
}
