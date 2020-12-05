package com.github.cato447.AbizeitungVotingSystem.entities;

import javax.persistence.*;

@Entity
@Table(name="voters")
public class Voter {

    public Voter(){
        super();
    }

    public Voter(String email) {
        this.email = email;
        this.vote_status = false;
        this.candidatesubmit_status = false;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Boolean vote_status;
    private Boolean candidatesubmit_status;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getVote_status() {
        return vote_status;
    }

    public Boolean getCandidatesubmit_status() {
        return candidatesubmit_status;
    }

    public void vote(){
        vote_status = true;
    }

    public void submitCandidates()  {
        candidatesubmit_status = true;
    }
}
