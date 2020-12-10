package com.github.cato447.AbizeitungVotingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name = "mottos")
public class Motto implements Comparable<Motto>{

    public Motto() {
        super();
    }

    public Motto(String name) {
        super();
        this.name = name;
        this.votes = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public void voteFor() {
        this.votes += 1;
    }

    @Override
    public int compareTo(Motto m) {
        if (getVotes() == null || m.getVotes() == null) {
            return 0;
        }
        return m.getVotes().compareTo(getVotes());
    }
}
