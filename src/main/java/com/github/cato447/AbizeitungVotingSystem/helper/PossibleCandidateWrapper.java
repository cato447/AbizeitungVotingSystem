package com.github.cato447.AbizeitungVotingSystem.helper;

import com.github.cato447.AbizeitungVotingSystem.entities.PossibleCandidate;

import java.util.LinkedList;


public class PossibleCandidateWrapper {

    private LinkedList<PossibleCandidate> possibleCandidates;

    public PossibleCandidateWrapper(){
        possibleCandidates = new LinkedList<>();
    }

    public void addPossibleCandidate(PossibleCandidate possibleCandidate){
        this.possibleCandidates.add(possibleCandidate);
    }

    public LinkedList<PossibleCandidate> getPossibleCandidates() {
        return possibleCandidates;
    }

    public void setPossibleCandidates(LinkedList<PossibleCandidate> possibleCandidates) {
        this.possibleCandidates = possibleCandidates;
    }
}
