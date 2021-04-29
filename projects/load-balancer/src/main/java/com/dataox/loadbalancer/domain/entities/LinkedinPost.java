package com.dataox.loadbalancer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "linkedin_post")
@NoArgsConstructor
public class LinkedinPost {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @NotBlank
    @Column(name = "item_source")
    private String itemSource;

    @Column(name = "relative_publication_date")
    private String relativePublicationDate;

    @NotNull
    @Column(name = "collected_date")
    private Instant collectedDate;

    @Column(name = "absolute_publication_date")
    private Instant absolutePublicationDate;

    @NotBlank
    @Column(name = "author_name")
    private String authorProfileName;

    @NotBlank
    @Column(name = "author_profile_url")
    private String authorProfileUrl;

    @Column(name = "author_connection_degree")
    private String authorConnectionDegree;

    @NotBlank
    @Column(name = "author_headline")
    private String authorHeadline;

    @Column(name = "content")
    private String content;

    @Column(name = "number_of_comments")
    private int numberOfComments;

    @Column(name = "number_of_reactions")
    private int numberOfReactions;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_activity_id", referencedColumnName = "id")
    private LinkedinActivity linkedinActivity;

    @OneToMany(mappedBy = "linkedinPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinComment> linkedinComments = new ArrayList<>();
}
