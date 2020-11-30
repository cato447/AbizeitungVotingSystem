package com.github.cato447.AbizeitungVotingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "possibleCandidates")
public class PossibleCandidate{

    public PossibleCandidate() {
        super();
    }

    public PossibleCandidate(String name, Category category) {
        super();
        this.name = name;
        this.category = category;
        this.votes = 1;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int votes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {return category;}

    public int getVotes() {
        return votes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
