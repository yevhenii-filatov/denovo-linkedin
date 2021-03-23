package com.dataox.loadbalancer.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    @Column(name = "search_page_source")
    private String searchPageSource;

    @NotNull
    @Column(name = "query_url")
    private String queryUrl;

    @OneToOne(mappedBy = "searchMetadata")
    private InitialData initialData;
}
