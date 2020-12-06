package com.github.cato447.AbizeitungVotingSystem.repositories;

import com.github.cato447.AbizeitungVotingSystem.entities.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AuthCodesRepository extends JpaRepository<AuthCode, Integer> {

    public AuthCode findByName(String name);

}
