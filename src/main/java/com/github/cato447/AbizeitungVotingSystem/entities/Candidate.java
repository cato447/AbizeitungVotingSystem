package com.github.cato447.AbizeitungVotingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "candidates")
public class Candidate {

    public Candidate() {
        super();
    }

    public Candidate(String name) {
        super();
        this.name = name;
        this.votes = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer votes;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getVotes() {
        return votes;
    }

    public Category getCategory() {return category;}
}
