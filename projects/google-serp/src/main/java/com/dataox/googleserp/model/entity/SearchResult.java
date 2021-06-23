package com.dataox.googleserp.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.Instant;

/**
 * @author Yevhenii Filatov
 * @since 12/23/20
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "google_search_result")
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

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "search_position")
    private int searchPosition;

    @NotNull
    @Column(name = "search_step")
    private int searchStep;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
//    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @JoinColumn(name = "initial_data_record_id", nullable = false)
    private InitialData initialDataRecord;

    @PrePersist
    private void setCollectedAt() {
        this.collectedAt = Instant.now();
    }
}
