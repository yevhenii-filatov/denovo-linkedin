package com.dataox.loadbalancer.domain.entities;

import com.dataox.linkedinscraper.dto.OptionalFieldsContainer;
import com.dataox.loadbalancer.domain.converter.OptionalFieldsConverter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Dmitriy Lysko
 * @since 23/03/2021
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Linkedin_not_reusable_profile")
@NoArgsConstructor
public class LinkedinNotReusableProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    @NotNull
    @Column(name = "error_description", columnDefinition = "TEXT")
    String errorDescription;

//    @NotNull
    @OneToOne
    @JoinColumn(name = "search_result_id", referencedColumnName = "id")
    SearchResult searchResult;

//    @NotNull
    @Column(name = "optional_fields")
    @Convert(converter = OptionalFieldsConverter.class)
    OptionalFieldsContainer optionalFieldsContainer;
}
