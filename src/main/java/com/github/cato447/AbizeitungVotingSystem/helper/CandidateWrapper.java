package com.github.cato447.AbizeitungVotingSystem.helper;

import com.github.cato447.AbizeitungVotingSystem.entities.Candidate;

import java.util.LinkedList;


public class CandidateWrapper {

    private LinkedList<Candidate> candidates;

    public CandidateWrapper(){
        candidates = new LinkedList<>();
    }

    public void addCandidate(Candidate candidate){
        this.candidates.add(candidate);
    }

    public LinkedList<Candidate> getCandidates() {
        return candidates;
    }
}
