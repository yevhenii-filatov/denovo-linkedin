package com.dataox.googleserp.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Yevhenii Filatov
 * @since 1/20/21
 */

@Getter
@Setter
@Entity
@Table(name = "search_metadata")
public class SearchMetadata {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "search_page_source", columnDefinition="TEXT")
    private String searchPageSource;

    @NotNull
    @Column(name = "query_url", columnDefinition="TEXT")
    private String queryUrl;

    @OneToOne(mappedBy = "searchMetadata")
    private InitialData initialData;
}
