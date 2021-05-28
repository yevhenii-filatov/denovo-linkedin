package com.dataox.loadbalancer.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_comment")
@NoArgsConstructor
public class LinkedinComment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

//    @NotBlank
    @Column(name = "item_source", columnDefinition = "TEXT")
    private String itemSource;

//    @NotBlank
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

//    @NotBlank
    @Column(name = "relative_publication_date", columnDefinition = "TEXT")
    private String relativePublicationDate;

//    @NotNull
    @Column(name = "collected_date")
    private Instant collectedDate;

//    @NotNull
    @Column(name = "absolute_publication_date")
    private Instant absolutePublicationDate;

    @Column(name = "number_of_reactions")
    private int numberOfReactions;

    @Column(name = "number_of_replies")
    private int numberOfReplies;

//    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_post_id", referencedColumnName = "id")
    private LinkedinPost linkedinPost;
}
