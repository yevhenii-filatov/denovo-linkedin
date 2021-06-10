package com.dataox.loadbalancer.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "google_search_metadata")
public class SearchMetadata {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
    @Column(name = "search_page_source", columnDefinition = "MEDIUMTEXT")
    private String searchPageSource;

//    @NotNull
    @Column(name = "query_url", columnDefinition = "varchar(1000)")
    private String queryUrl;

    @OneToOne(mappedBy = "searchMetadata")
    private InitialData initialData;
}
