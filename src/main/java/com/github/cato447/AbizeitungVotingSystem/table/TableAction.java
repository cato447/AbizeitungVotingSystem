package com.github.cato447.AbizeitungVotingSystem.table;

import com.github.cato447.AbizeitungVotingSystem.controller.VotingController;
import com.github.cato447.AbizeitungVotingSystem.entities.*;
import com.github.cato447.AbizeitungVotingSystem.repositories.*;
import org.aspectj.weaver.loadtime.definition.LightXMLParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class TableAction {

    public TableAction(){

    }

    public void updateVotingStatus(String email, VoterRepository voterRepository){
        Voter voter = voterRepository.findByEmail(email);
        voter.vote();
        voterRepository.save(voter);
    }

    public void updateCandidatesubmit_status(String email, VoterRepository voterRepository){
        Voter voter = voterRepository.findByEmail(email);
        voter.submitCandidates();
        voterRepository.save(voter);
    }

    public AuthCode generateToken(String name, String code, AuthCodesRepository authCodesRepository) {
        AuthCode authCode = new AuthCode(name, code);
        try{
            AuthCode existingCode = authCodesRepository.findByName(authCode.getName());
            existingCode.setCode(code);
            existingCode.setTime(System.currentTimeMillis());
            authCodesRepository.save(existingCode);
            return existingCode;
        } catch (Exception e){
            authCodesRepository.save(authCode);
            return authCode;
        }
    }

    public String checkToken(String name, String code, AuthCodesRepository authCodesRepository){
        AuthCode authCode = authCodesRepository.findByName(name);
        if (authCode.getCode().equals(code) && !fiveMinutesPassed(authCode.getTime())){
            authCodesRepository.delete(authCode);
            return "matched";
        } else if(fiveMinutesPassed(authCode.getTime())) {
            authCodesRepository.delete(authCode);
            return "expired";
        } else {
            return "wrong";
        }
    }

    private boolean fiveMinutesPassed(Long time){
        return System.currentTimeMillis() >= (time + 300*1000);
    }

    private int getLimit(List<PossibleCandidate> possibleCandidates){
        return possibleCandidates.size() <= 5 ? possibleCandidates.size() : 5;
    }

    public void voteFor(String id, CandidateRepository candidateRepository){
        long candidateID = Long.valueOf(id);
        Candidate candidate = candidateRepository.findById(candidateID).get();
        candidate.votedFor();
        candidateRepository.save(candidate);
    }

    public void setUpVoters(VoterRepository voterRepository){
        try {
            String path = "src/main/resources/Q2_emails.txt";
            File emailFile = new File(path);
            Scanner myReader = new Scanner(emailFile);
            ArrayList<Voter> voters = new ArrayList<Voter>();
            while (myReader.hasNextLine()) {
                String email = myReader.nextLine();
                Voter voter = new Voter(email);
                voters.add(voter);
            }
            voterRepository.saveAll(voters);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void setUpCandidates(PossibleCandidateRepository possibleCandidateRepository, CandidateRepository candidateRepository){
        List<PossibleCandidate> possibleCandidates = possibleCandidateRepository.findAll();
        Collections.sort(possibleCandidates, Comparator.comparing(PossibleCandidate::getCategoryID));
        long category_id = possibleCandidates.get(0).getCategory().getId();
        List<PossibleCandidate> possibleCandidatesPerCategory = new LinkedList<>();
        for (int i = 0; i < possibleCandidates.size(); i++) {
            PossibleCandidate p = possibleCandidates.get(i);
            if (category_id == p.getCategory().getId()){
                possibleCandidatesPerCategory.add(p);
            } else {
                category_id = p.getCategory().getId();
                Collections.sort(possibleCandidatesPerCategory, Comparator.comparing(PossibleCandidate::getVotes));
                Collections.reverse(possibleCandidatesPerCategory);
                for (int j = 0; j < getLimit(possibleCandidatesPerCategory); j++){
                    Candidate candidate = new Candidate(possibleCandidatesPerCategory.get(j).getName(), possibleCandidatesPerCategory.get(j).getCategory());
                    candidateRepository.save(candidate);
                }
                i += -1;
                possibleCandidatesPerCategory.clear();
            }
        }
        for (int j = 0; j < getLimit(possibleCandidatesPerCategory); j++){
            Candidate candidate = new Candidate(possibleCandidatesPerCategory.get(j).getName(), possibleCandidatesPerCategory.get(j).getCategory());
            candidateRepository.save(candidate);
        }
    }

    public void setUpCategories(CategoryRepository categoryRepository){
        try {
            String path = "src/main/resources/Categories.txt";
            File categoryFile = new File(path);
            Scanner myReader = new Scanner(categoryFile);
            ArrayList<Category> categories = new ArrayList<Category>();
            while (myReader.hasNextLine()) {
                String name = myReader.nextLine();
                Category category = new Category(name);
                categories.add(category);
            }
            categoryRepository.saveAll(categories);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
