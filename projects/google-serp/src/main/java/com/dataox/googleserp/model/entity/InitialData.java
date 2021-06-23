package com.dataox.googleserp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Yevhenii Filatov
 * @since 12/23/20
 */

@Getter
@Setter
@Entity
@Table(name = "google_initial_data")
@ToString(exclude = "searchResults")
@JsonIgnoreProperties(value = {"searchMetadata", "searchResults"})
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
    @Column(name = "middle_name")
    private String middleName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "firm_name")
    private String firmName;

    @Column(name = "linkedin_url", length = 1000)
    private String linkedinUrl;

    @NotNull
    @Column(name = "searched")
    private Boolean searched;

    @Column(name = "no_results")
    private Boolean noResults;

//    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "search_metadata_id", referencedColumnName = "id")
    private SearchMetadata searchMetadata;

//    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OneToMany(mappedBy = "initialDataRecord", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SearchResult> searchResults = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitialData that = (InitialData) o;
        return Objects.equals(denovoId, that.denovoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(denovoId);
    }
}
