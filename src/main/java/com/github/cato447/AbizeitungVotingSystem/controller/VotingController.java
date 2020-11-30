package com.github.cato447.AbizeitungVotingSystem.controller;

import com.github.cato447.AbizeitungVotingSystem.entities.Category;
import com.github.cato447.AbizeitungVotingSystem.entities.PossibleCandidate;
import com.github.cato447.AbizeitungVotingSystem.entities.Voter;
import com.github.cato447.AbizeitungVotingSystem.helper.PossibleCandidateWrapper;
import com.github.cato447.AbizeitungVotingSystem.repositories.CandidateRepository;
import com.github.cato447.AbizeitungVotingSystem.repositories.CategoryRepository;
import com.github.cato447.AbizeitungVotingSystem.repositories.PossibleCandidateRepository;
import com.github.cato447.AbizeitungVotingSystem.repositories.VoterRepository;
import com.github.cato447.AbizeitungVotingSystem.table.TableAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class VotingController {

    private Boolean candidatesAdded = false;

    private static final Logger LOGGER = LogManager.getLogger(VotingController.class);

    private TableAction tableAction = new TableAction();

    List<String> ipAddresses = new ArrayList<String>();

    @Autowired
    VoterRepository voterRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PossibleCandidateRepository possibleCandidateRepository;

    @Autowired
    JavaMailSender emailSender;


    @PostConstruct
    public void init() {
        LOGGER.info("setups");
        if (voterRepository.findAll().size() == 0) {
            tableAction.setUpVoters(voterRepository);
            LOGGER.info("Voters successfully set up");
        }

        if (categoryRepository.findAll().size() == 0){
            tableAction.setUpCategories(categoryRepository);
            LOGGER.info("Categories successfully set up");
        }

        if (candidateRepository.findAll().size() == 0) {
            tableAction.setUpCandidates(candidateRepository, categoryRepository);
            LOGGER.info("Candidates successfully set up");
        }
    }

    @RequestMapping("/")
    public String WelcomeSite() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        String currentIpAddress = request.getRemoteAddr();
        if (!this.ipAddresses.contains(currentIpAddress)) {
            LOGGER.info("User IP: " + request.getRemoteAddr());
            ipAddresses.add(currentIpAddress);
        }
        return "start.html";
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @RequestMapping("/vote")
    public String VerifyName(@RequestParam String name, Model model) {
        if (name.strip().toLowerCase().matches("[a-z]+\\.[a-z]+@adolfinum+\\.de$")) {
            try {
                Voter voter = voterRepository.findByEmail(name.toLowerCase().strip());
                LOGGER.warn(voter.getEmail());
                if (voter.getVote_status()) {
                    LOGGER.warn(name + " has already voted");
                    return "errors/alreadyVoted.html";
                } else if (voter.getCandidatesubmit_status()) {
                    LOGGER.warn(name + " has already submitted its candidates");
                    return "errors/alreadysubmittedcandidates.html";
                } else {
                    if(candidatesAdded) {
                        List<Category> categories = categoryRepository.findAll();
                        model.addAttribute("categories", categories);
                        model.addAttribute("name", name);
                        //sendSimpleMessage(name,"test", "test");
                        LOGGER.info(name + " is voting now");
                        return "voting.html";
                    } else {
                        PossibleCandidateWrapper possibleCandidates = new PossibleCandidateWrapper();
                        List<Category> categories = categoryRepository.findAll();
                        for (int i = 0; i < categories.size(); i++){
                            possibleCandidates.addPossibleCandidate(new PossibleCandidate());
                        }
                        model.addAttribute("categories", categories);
                        model.addAttribute("form", possibleCandidates);
                        LOGGER.info(name + " is submitting candidates");
                        return "addingCandidates.html";
                    }
                }
            } catch (Exception e) {
                LOGGER.error(name + " is not allowed to vote");
                return "errors/notRegistered.html";
            }
        } else {
            return "errors/falseInput";
        }
    }

    @RequestMapping("/saveCandidates")
    public String candidateSaving(@ModelAttribute PossibleCandidateWrapper possibleCandidates){
        LinkedList<PossibleCandidate> posCandidates = possibleCandidates.getPossibleCandidates();
        long index = 1;
        for (PossibleCandidate posCandidate : posCandidates){
            if (posCandidate.getName() != "") {
                if (possibleCandidateRepository.findByNameAndCategory(posCandidate.getName(), categoryRepository.findById(index).get()) != null) {
                    PossibleCandidate p = possibleCandidateRepository.findByNameAndCategory(posCandidate.getName(), categoryRepository.findById(index).get());
                    LOGGER.warn(p.getVotes());
                    p.setVotes(p.getVotes() + 1);
                    possibleCandidateRepository.save(p);
                } else {
                    PossibleCandidate possibleCandidate = new PossibleCandidate(posCandidate.getName(), categoryRepository.findById(index).get());
                    possibleCandidateRepository.save(possibleCandidate);
                }
            }
            index++;
        }
        return "candidateAddingSuccessful.html";
    }

    @RequestMapping("/processVote")
    public String ProcessVote(@RequestParam String voteValues, @RequestParam String voterEmail) {
        String[] partVoteValues = voteValues.split(",");
        for (String s: partVoteValues) {
            tableAction.voteFor(s, candidateRepository);
        }
        tableAction.updateVotingStatus(voterEmail,voterRepository);
        LOGGER.info(voterEmail + " has voted!");
        return "voteSuccessful.html";
    }

    @RequestMapping("/dashboard")
    public String AccessDashboard(@RequestParam String password, Model model){
        try {
                if (password.equals("admin")) {
                    List<Voter> voters = voterRepository.findAll();
                    List<Category> categories = categoryRepository.findAll();
                    model.addAttribute("voters", voters);
                    model.addAttribute("categories", categories);
                    return "dashboard.html";
                } else {
                    LOGGER.error("Wrong Password");
                }
        } catch (Exception e) {
            LOGGER.fatal("voters table is not existing!");
        }
        return "redirect:/";
    }

}
