package com.github.cato447.AbizeitungVotingSystem.repositories;

import com.github.cato447.AbizeitungVotingSystem.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    public Candidate findByName(String name);

}
