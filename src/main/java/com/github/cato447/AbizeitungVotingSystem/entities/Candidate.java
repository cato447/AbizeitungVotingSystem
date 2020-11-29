package com.github.cato447.AbizeitungVotingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "candidates")
public class Candidate implements Comparable<Candidate>{

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

    public void setName(String name) {
        this.name = name;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void votedFor() {
        this.votes += 1;
    }

    @Override
    public int compareTo(Candidate c) {
        if (getVotes() == null || c.getVotes() == null) {
            return 0;
        }
        return c.getVotes().compareTo(getVotes());
    }
}
