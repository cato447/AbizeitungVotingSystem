package com.github.cato447.AbizeitungVotingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaRepositories
public class AbizeitungVotingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbizeitungVotingSystemApplication.class, args);
	}

}