package com.dataox.googleserp.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Yevhenii Filatov
 * @since 1/20/21
 */

@Getter
@Setter
@Entity
@Table(name = "google_search_metadata")
public class SearchMetadata {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "search_page_source", columnDefinition="MEDIUMTEXT")
    private String searchPageSource;

    @NotNull
    @Column(name = "query_url", columnDefinition="varchar(1000)")
    private String queryUrl;

//    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OneToOne(mappedBy = "searchMetadata", cascade = CascadeType.ALL)
    private InitialData initialData;
}
