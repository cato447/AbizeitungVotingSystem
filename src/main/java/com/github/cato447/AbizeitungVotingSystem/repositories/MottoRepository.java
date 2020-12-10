package com.github.cato447.AbizeitungVotingSystem.repositories;

import com.github.cato447.AbizeitungVotingSystem.entities.Motto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MottoRepository extends JpaRepository<Motto, Integer> {

    public Motto findByName(String name);

    Optional<Motto> findById(Long id);

}
