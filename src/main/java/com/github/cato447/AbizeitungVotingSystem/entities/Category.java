package com.github.cato447.AbizeitungVotingSystem.entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name="categories")
public class Category {

    public Category(){
        super();
    }

    public Category(String name, List<Candidate> candidateList) {
        super();
        this.name = name;
        this.candidateList = candidateList;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Candidate> candidateList;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Candidate> getCandidateList() {
        Collections.sort(candidateList);
        return candidateList;
    }

    public int getCandidateListSize(){
        return candidateList.size();
    }
}
