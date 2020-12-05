package com.github.cato447.AbizeitungVotingSystem.repositories;

import com.github.cato447.AbizeitungVotingSystem.entities.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodesRepository extends JpaRepository<AuthCode, Integer> {

    public AuthCode findByName(String name);

}
