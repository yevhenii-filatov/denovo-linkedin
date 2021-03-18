package com.dataox.loadbalancer.domain.entities;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "search_result")
public class SearchResult {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "collected_at", updatable = false)
    private Instant collectedAt;

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "initial_data_record_id")
    private InitialData initialDataRecord;

    @ManyToOne
    @JoinColumn(name = "scraping_batch_id",nullable = true)
    private ScrapingBatch scrapingBatch;

    @OneToOne(mappedBy = "searchResult", orphanRemoval = true, fetch = FetchType.LAZY)
    LinkedinProfile linkedinProfile;
}
