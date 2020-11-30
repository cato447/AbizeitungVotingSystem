package com.github.cato447.AbizeitungVotingSystem.repositories;

import com.github.cato447.AbizeitungVotingSystem.entities.PossibleCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PossibleCandidateRepository extends JpaRepository<PossibleCandidate, Integer> {

    public PossibleCandidate findByNameAndCategoryID(String name, Long category_id);
    Optional<PossibleCandidate> findById(Long id);


}