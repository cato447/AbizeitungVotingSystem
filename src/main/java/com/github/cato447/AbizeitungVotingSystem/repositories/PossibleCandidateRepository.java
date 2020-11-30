package com.github.cato447.AbizeitungVotingSystem.repositories;

import com.github.cato447.AbizeitungVotingSystem.entities.Category;
import com.github.cato447.AbizeitungVotingSystem.entities.PossibleCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PossibleCandidateRepository extends JpaRepository<PossibleCandidate, Integer> {

    public PossibleCandidate findByNameAndCategory(String name, Category category);
    Optional<PossibleCandidate> findById(Long id);


}