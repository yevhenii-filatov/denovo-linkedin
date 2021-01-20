package com.dataox.googleserp.model.entity;

import com.sun.istack.NotNull;
import lombok.*;

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
@Table(name = "search_result")
public class SearchResult {
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
    @ManyToOne
    @JoinColumn(name = "initial_data_record_id", nullable = false)
    private InitialData initialDataRecord;
}
