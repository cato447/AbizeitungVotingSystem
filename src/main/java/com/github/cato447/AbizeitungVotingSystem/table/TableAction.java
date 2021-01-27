package com.github.cato447.AbizeitungVotingSystem.table;

import com.github.cato447.AbizeitungVotingSystem.entities.*;
import com.github.cato447.AbizeitungVotingSystem.repositories.*;

import java.io.*;
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
        try {
            AuthCode authCode = authCodesRepository.findByName(name);
            if (authCode.getCode().equals(code) && !authCode.isExpired()) {
                authCodesRepository.delete(authCode);
                return "matched";
            } else if (authCode.isExpired()) {
                authCodesRepository.delete(authCode);
                return "expired";
            }
        } catch(Exception e){
            return "wrong";
        }
        return "wrong";
    }

    public void deleteToken(String name, AuthCodesRepository authCodesRepository){
        authCodesRepository.delete(authCodesRepository.findByName(name));
    }

    private int getLimit(List<PossibleCandidate> possibleCandidates){
        return possibleCandidates.size() <= 15 ? possibleCandidates.size() : 15;
    }

    public void voteForCandidate(String id, CandidateRepository candidateRepository){
        long candidateID = Long.valueOf(id);
        Candidate candidate = candidateRepository.findById(candidateID).get();
        candidate.votedFor();
        candidateRepository.save(candidate);
    }

    public void setUpVoters(VoterRepository voterRepository){
        try (InputStream inputStream = getClass().getResourceAsStream("/Email_Whitelist.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = "";
            ArrayList<Voter> voters = new ArrayList<Voter>();
            while ((line = reader.readLine())!= null){
                String email = line;
                Voter voter = new Voter(email);
                voters.add(voter);
            }
            voterRepository.saveAll(voters);
        } catch (IOException e) {
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
                    if (j >= 10 && possibleCandidatesPerCategory.get(j).getVotes() == possibleCandidatesPerCategory.get(j-1).getVotes()){
                        Candidate candidate = new Candidate(possibleCandidatesPerCategory.get(j).getName(), possibleCandidatesPerCategory.get(j).getCategory());
                        candidateRepository.save(candidate);
                    }
                    if (j < 10){
                        Candidate candidate = new Candidate(possibleCandidatesPerCategory.get(j).getName(), possibleCandidatesPerCategory.get(j).getCategory());
                        candidateRepository.save(candidate);
                    }
                    if (j == 14){
                        break;
                    }
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
        try (InputStream inputStream = getClass().getResourceAsStream("/Categories.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = "";
            ArrayList<Category> categories = new ArrayList<Category>();
            while ((line = reader.readLine())!= null){
                String name = line;
                Category category = new Category(name);
                categories.add(category);
            }
            categoryRepository.saveAll(categories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
