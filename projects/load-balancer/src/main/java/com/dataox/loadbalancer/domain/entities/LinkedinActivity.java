package com.dataox.loadbalancer.domain.entities;

import com.dataox.loadbalancer.domain.types.LinkedinActivityType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@Entity
@Table(name = "linkedin_activity")
@NoArgsConstructor
public class LinkedinActivity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    LinkedinActivityType linkedinActivityType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id",referencedColumnName = "id")
    private LinkedinProfile linkedinProfile;

    @OneToOne(mappedBy = "linkedinActivity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private LinkedinPost linkedinPost;
}
