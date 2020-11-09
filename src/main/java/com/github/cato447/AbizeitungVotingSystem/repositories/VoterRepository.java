package com.github.cato447.AbizeitungVotingSystem.repositories;

import com.github.cato447.AbizeitungVotingSystem.entities.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoterRepository extends JpaRepository<Voter, Integer> {

    public Voter findByEmail(String email);

    public Voter findById(Long id);

}
