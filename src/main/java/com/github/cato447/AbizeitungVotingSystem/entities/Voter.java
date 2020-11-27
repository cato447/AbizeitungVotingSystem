package com.github.cato447.AbizeitungVotingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name="voters")
public class Voter {

    public Voter(){
        super();
    }

    public Voter(String email, boolean vote_status) {
        this.email = email;
        this.vote_status= vote_status;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Boolean vote_status;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getVote_status() {
        return vote_status;
    }

    public void vote(){
        vote_status = true;
    }
}
