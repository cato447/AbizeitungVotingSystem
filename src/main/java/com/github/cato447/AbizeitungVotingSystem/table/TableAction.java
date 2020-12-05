package com.github.cato447.AbizeitungVotingSystem.table;

import com.github.cato447.AbizeitungVotingSystem.controller.VotingController;
import com.github.cato447.AbizeitungVotingSystem.entities.*;
import com.github.cato447.AbizeitungVotingSystem.repositories.*;
import org.aspectj.weaver.loadtime.definition.LightXMLParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TableAction {

    public TableAction(){

    }

    public void addCandidate(String name, long category_id, CategoryRepository categoryRepository,CandidateRepository candidateRepository){
        Candidate candidate = new Candidate(name, categoryRepository.findById(category_id).get());
        candidateRepository.save(candidate);
    }

    public void addPossibleCandidate(String name, long category_id, CategoryRepository categoryRepository, PossibleCandidateRepository possibleCandidateRepository){
        PossibleCandidate possibleCandidate = new PossibleCandidate(name, categoryRepository.findById(category_id).get());
        possibleCandidateRepository.save(possibleCandidate);
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
        if (authCodesRepository.findByName(authCode.getName()) != null) {
            authCodesRepository.findByName(authCode.getName()).setCode(authCode.getCode());
            return authCode;
        } else {
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

    public void voteFor(String id, CandidateRepository candidateRepository){
        long candidateID = Long.valueOf(id);
        Candidate candidate = candidateRepository.findById(candidateID).get();
        candidate.votedFor();
        candidateRepository.save(candidate);
    }

    public void setUpVoters(VoterRepository voterRepository){
        try {
            File emailFile = new File("src/main/resources/Q2_emails.txt");
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

    public void setUpCandidates(CandidateRepository candidateRepository, CategoryRepository categoryRepository){
        ArrayList<String> names = new ArrayList<>();
        Collections.addAll(names, "Greta Bentgens", "Laura König", "Aaron Glos", "Lukas Boy", "Frau Meyering"
                , "Frau Adams", "Herr Petering", "Frau Milde", "Frau Meyer");

        ArrayList<Candidate> candidates = new ArrayList<>();
        for (String name: names) {
            Candidate candidate = new Candidate(name, categoryRepository.findById(20l).get());
            candidates.add(candidate);
        }
        candidateRepository.saveAll(candidates);
    }

    public void setUpCategories(CategoryRepository categoryRepository){
        ArrayList<String> names = new ArrayList<>();
        try {
            File categoryFile = new File("src/main/resources/Categories.txt");
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

    public String logCategories(CategoryRepository categoryRepository){
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("Id", "Name", "Candidates");
        rows.add(headers);

        for (Category category: categoryRepository.findAll()) {
            String candidateNames = "";
            for (Candidate candidate: category.getCandidateList()){
                candidateNames += candidate.getName() + "; ";
            }
            rows.add(Arrays.asList(""+category.getId(), category.getName(), candidateNames));
        }
        return formatAsTable(rows);
    }

    public String logVoters(VoterRepository voterRepository){
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("Id", "E-Mail", "Vote_status");
        rows.add(headers);
        for (Voter voter: voterRepository.findAll()) {
            rows.add(Arrays.asList(""+voter.getId(), voter.getEmail(), ""+voter.getVote_status()));
        }

        return formatAsTable(rows);

    }

    public String logCandidatesRepository(CandidateRepository candidateRepository){
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("Id", "Name", "Votes");
        rows.add(headers);
        for (Candidate candidate: candidateRepository.findAll()) {
            rows.add(Arrays.asList(""+candidate.getId(), candidate.getName(), ""+candidate.getVotes()));
        }

        return formatAsTable(rows);
    }

    public String logCandidates(LinkedList<Candidate> candidates, CategoryRepository categoryRepository){
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("Id", "Name", "Votes", "Category_ID");
        rows.add(headers);
        long i = 1;
        for (Candidate candidate: candidates) {
                Category category = categoryRepository.findById(i).get();
                rows.add(Arrays.asList("" + i, candidate.getName(), "" + 0, "" + category.getId()));
                i++;
        }

        return formatAsTable(rows);
    }

    public String logPossibleCandidates(LinkedList<PossibleCandidate> possibleCandidates, CategoryRepository categoryRepository){
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("Id", "Name", "Votes", "Category_ID");
        rows.add(headers);
        long i = 1;
        for (PossibleCandidate possibleCandidate: possibleCandidates) {
            Category category = categoryRepository.findById(i).get();
            rows.add(Arrays.asList("" + i, possibleCandidate.getName(), "" + 0, "" + category.getId()));
            i++;
        }

        return formatAsTable(rows);
    }

    private String formatAsTable(List<List<String>> rows) {
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows)
        {
            for (int i = 0; i < row.size(); i++)
            {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths)
        {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        result.append("\n");
        for (List<String> row : rows)
        {
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }
        return result.toString();
    }
}
