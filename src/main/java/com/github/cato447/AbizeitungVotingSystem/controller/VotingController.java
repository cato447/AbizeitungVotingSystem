package com.github.cato447.AbizeitungVotingSystem.controller;

import com.github.cato447.AbizeitungVotingSystem.entities.Candidate;
import com.github.cato447.AbizeitungVotingSystem.entities.Category;
import com.github.cato447.AbizeitungVotingSystem.entities.Voter;
import com.github.cato447.AbizeitungVotingSystem.repositories.CandidateRepository;
import com.github.cato447.AbizeitungVotingSystem.repositories.CategoryRepository;
import com.github.cato447.AbizeitungVotingSystem.repositories.VoterRepository;
import com.github.cato447.AbizeitungVotingSystem.table.TableAction;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class VotingController {

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
    JavaMailSender emailSender;

    @RequestMapping("/")
    public String WelcomeSite() {

        if (voterRepository.findAll().size() == 0) {
            tableAction.setUpVoters(voterRepository);
            LOGGER.info("Voters successfully set up");
        }
        if (candidateRepository.findAll().size() == 0) {
            tableAction.setUpCandidates(candidateRepository);
            LOGGER.info("Candidates successfully set up");
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        String currentIpAddress = request.getRemoteAddr();
        if (!this.ipAddresses.contains(currentIpAddress)) {
            LOGGER.info("User IP: " + request.getRemoteAddr());
            ipAddresses.add(currentIpAddress);
        }

        tableAction.logVoters(voterRepository);
        tableAction.logCandidates(candidateRepository);
        tableAction.logCategories(categoryRepository);

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
                if (voter.getVote_status()) {
                    LOGGER.warn(name + " has already voted");
                    return "errors/alreadyVoted.html";
                } else {
                    List<Candidate> candidates = candidateRepository.findAll();
                    List<Category> categories = categoryRepository.findAll();
                    model.addAttribute("candidates", candidates);
                    model.addAttribute("categories", categories);
                    model.addAttribute("name", name);
                    //sendSimpleMessage(name,"test", "test");
                    LOGGER.info(name + " is voting now");
                    return "voting.html";
                }
            } catch (Exception e) {
                LOGGER.error(name + " is not allowed to vote");
                return "errors/notRegistered.html";
            }
        } else {
            return "errors/falseInput";
        }
    }

    @RequestMapping("/processVote")
    public String ProcessVote(@RequestParam String voteValues, @RequestParam String voterEmail) {
        String[] partVoteValues = voteValues.split(",");
        for (String s: partVoteValues) {
            long candidateID = Long.valueOf(s);
            Candidate candidate = candidateRepository.findById(candidateID).get();
            candidate.votedFor();
            candidateRepository.save(candidate);
        }
        Voter voter = voterRepository.findByEmail(voterEmail);
        voter.vote();
        voterRepository.save(voter);
        LOGGER.info(voterEmail + " has voted!");
        return "success.html";
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
