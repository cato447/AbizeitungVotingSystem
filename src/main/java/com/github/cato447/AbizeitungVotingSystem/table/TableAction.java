package com.github.cato447.AbizeitungVotingSystem.table;

import com.github.cato447.AbizeitungVotingSystem.entities.Candidate;
import com.github.cato447.AbizeitungVotingSystem.entities.Category;
import com.github.cato447.AbizeitungVotingSystem.entities.Voter;
import com.github.cato447.AbizeitungVotingSystem.repositories.CandidateRepository;
import com.github.cato447.AbizeitungVotingSystem.repositories.CategoryRepository;
import com.github.cato447.AbizeitungVotingSystem.repositories.VoterRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TableAction {

    public TableAction(){

    }

    public void addCandidate(String name, CandidateRepository candidateRepository){
        Candidate candidate = new Candidate(name);
        candidateRepository.save(candidate);
    }

    public void setUpVoters(VoterRepository voterRepository){
        try {
            File emailFile = new File("src/main/resources/Q2_emails.txt");
            Scanner myReader = new Scanner(emailFile);
            ArrayList<Voter> voters = new ArrayList<Voter>();
            while (myReader.hasNextLine()) {
                String email = myReader.nextLine();
                Voter voter = new Voter(email, false);
                voters.add(voter);
            }
            voterRepository.saveAll(voters);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void setUpCandidates(CandidateRepository candidateRepository){
        ArrayList<String> names = new ArrayList<>();
        Collections.addAll(names, "Greta Bentgens", "Laura König", "Aaron Glos", "Lukas Boy", "Frau Meyering"
                , "Frau Adams", "Herr Petering", "Frau Milde", "Frau Meyer");

        ArrayList<Candidate> candidates = new ArrayList<>();
        for (String name: names) {
            Candidate candidate = new Candidate(name);
            candidates.add(candidate);
        }
        candidateRepository.saveAll(candidates);
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

    public String logCandidates(CandidateRepository candidateRepository){
        List<List<String>> rows = new ArrayList<>();
        List<String> headers = Arrays.asList("Id", "Name", "Votes");
        rows.add(headers);
        for (Candidate candidate: candidateRepository.findAll()) {
            rows.add(Arrays.asList(""+candidate.getId(), candidate.getName(), ""+candidate.getVotes()));
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
