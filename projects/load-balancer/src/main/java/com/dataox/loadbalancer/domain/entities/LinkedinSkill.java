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
@Table(name = "linkedin_skill")
@NoArgsConstructor
public class LinkedinSkill {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

//    @NotBlank
    @Column(name = "item_source", columnDefinition = "TEXT")
    private String itemSource;

//    @NotBlank
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

//    @NotBlank
    @Column(name = "category", columnDefinition = "TEXT")
    private String category;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "number_of_endorsements")
    private int numberOfEndorsements;

//    @NotNull
    @ManyToOne
    @JoinColumn(name = "linkedin_profile_id", referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;

    @OneToMany(mappedBy = "linkedinSkill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LinkedinEndorsement> linkedinEndorsements = new ArrayList<>();

    @Override
    public String toString() {
        return "LinkedinSkill{" +
                "id=" + id +
                ", updatedAt=" + updatedAt +
                ", itemSource='" + itemSource + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", url='" + url + '\'' +
                ", numberOfEndorsements=" + numberOfEndorsements +
                ", linkedinProfile=" + linkedinProfile +
                ", linkedinProfile_id=" + linkedinProfile.getId() +
                '}';
    }
}
