package com.github.cato447.AbizeitungVotingSystem.repositories;

import com.github.cato447.AbizeitungVotingSystem.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Category findByName(String name);

}
