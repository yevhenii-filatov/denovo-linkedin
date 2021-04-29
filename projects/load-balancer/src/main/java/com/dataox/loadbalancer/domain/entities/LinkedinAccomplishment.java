package com.dataox.loadbalancer.domain.entities;

import com.dataox.loadbalancer.domain.types.LinkedinAccomplishmentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_accomplishment")
@NoArgsConstructor
public class LinkedinAccomplishment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotBlank
    @Column(name = "item_source")
    private String itemSource;

    @NotBlank
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LinkedinAccomplishmentType linkedinAccomplishmentType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id",referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;
}
