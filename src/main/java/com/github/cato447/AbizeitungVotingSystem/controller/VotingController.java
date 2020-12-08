package com.github.cato447.AbizeitungVotingSystem.controller;

import com.github.cato447.AbizeitungVotingSystem.entities.AuthCode;
import com.github.cato447.AbizeitungVotingSystem.entities.Category;
import com.github.cato447.AbizeitungVotingSystem.entities.PossibleCandidate;
import com.github.cato447.AbizeitungVotingSystem.entities.Voter;
import com.github.cato447.AbizeitungVotingSystem.helper.PossibleCandidateWrapper;
import com.github.cato447.AbizeitungVotingSystem.helper.RandomNumber;
import com.github.cato447.AbizeitungVotingSystem.repositories.*;
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

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
public class VotingController {


    private boolean votingPhase = false;

    private static final Logger LOGGER = LogManager.getLogger(VotingController.class);
    private TableAction tableAction = new TableAction();

    @Autowired
    VoterRepository voterRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PossibleCandidateRepository possibleCandidateRepository;

    @Autowired
    AuthCodesRepository authCodesRepository;

    @Autowired
    JavaMailSender emailSender;


    @PostConstruct
    public void init() {
        try {
            String mode = System.getProperty("votingPhase");
            if (mode.equalsIgnoreCase("true")) {
                votingPhase = true;
            } else {
                votingPhase = false;
            }
        } catch (Exception e){
            votingPhase = false;
        }

        if (voterRepository.findAll().size() == 0) {
            tableAction.setUpVoters(voterRepository);
            LOGGER.info("Voters successfully set up");
        }

        if (categoryRepository.findAll().size() == 0) {
            tableAction.setUpCategories(categoryRepository);
            LOGGER.info("Categories successfully set up");
        }

        if (candidateRepository.findAll().size() == 0 && votingPhase == true && possibleCandidateRepository.findAll().size() != 0) {
            tableAction.setUpCandidates(possibleCandidateRepository, candidateRepository);
            LOGGER.info("Candidates successfully set up");
        }
        LOGGER.info(votingPhase);
    }

    @RequestMapping("/")
    public String WelcomeSite() {
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

    @RequestMapping("/checkStatus")
    public String VerifyName(@RequestParam String name, Model model) {
        if (name.strip().toLowerCase().matches("[a-z]+\\.[a-z]+@adolfinum+\\.de$")) {
            try {
                LOGGER.warn(name);
                Voter voter = voterRepository.findByEmail(name.toLowerCase().strip());
                if (voter.getVote_status()) {
                    LOGGER.warn(name + " has already voted");
                    return "errors/alreadyVoted.html";
                } else if (voter.getCandidatesubmit_status() && votingPhase == false) {
                    LOGGER.warn(name + " has already submitted its candidates");
                    return "errors/alreadysubmittedcandidates.html";
                } else {
                    AuthCode authCode = tableAction.generateToken(name, RandomNumber.getRandomNumberString(), authCodesRepository);
                    sendSimpleMessage(name, "Code zur Authentifizierung", "Dein Code lautet: " + authCode.getCode());
                    model.addAttribute("name", name);
                    model.addAttribute("codeExpired", false);
                    model.addAttribute("codeFalse", false);
                    return "authenticate.html";
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(name + " is not allowed to vote");
                return "errors/notRegistered.html";
            }
        } else {
            return "errors/falseInput.html";
        }
    }

    @RequestMapping("/vote")
    public String voting_adding(@RequestParam String code, @RequestParam String name, Model model) {
        switch (tableAction.checkToken(name, code, authCodesRepository)) {
            case "matched":
                LOGGER.warn("matched");
                if (votingPhase) {
                    List<Category> categories = categoryRepository.findAll();
                    model.addAttribute("categories", categories);
                    model.addAttribute("name", name);
                    return "voting.html";
                } else {
                    PossibleCandidateWrapper possibleCandidates = new PossibleCandidateWrapper();
                    List<Category> categories = categoryRepository.findAll();
                    for (int i = 0; i < categories.size(); i++) {
                        possibleCandidates.addPossibleCandidate(new PossibleCandidate());
                    }
                    model.addAttribute("categories", categories);
                    model.addAttribute("form", possibleCandidates);
                    model.addAttribute("name", name);
                    return "addingCandidates.html";
                }

            case "expired":
                model.addAttribute("name", name);
                model.addAttribute("codeExpired", true);
                model.addAttribute("codeFalse", false);
                return "authenticate.html";

            case "wrong":
                model.addAttribute("name", name);
                model.addAttribute("codeExpired", false);
                model.addAttribute("codeFalse", true);
                return "authenticate.html";
        }
        return "fatalError";
    }

    @RequestMapping("/saveCandidates")
    public String candidateSaving(@ModelAttribute PossibleCandidateWrapper possibleCandidates, @RequestParam String name) {
        if (voterRepository.findByEmail(name).getVote_status()) {
            return "errors/alreadyVoted.html";
        } else {
            LinkedList<PossibleCandidate> posCandidates = possibleCandidates.getPossibleCandidates();
            long index = 1;
            for (PossibleCandidate posCandidate : posCandidates) {
                if (posCandidate.getName() != "") {
                    if (possibleCandidateRepository.findByNameAndCategory(posCandidate.getName(), categoryRepository.findById(index).get()) != null) {
                        PossibleCandidate p = possibleCandidateRepository.findByNameAndCategory(posCandidate.getName(), categoryRepository.findById(index).get());
                        p.setVotes(p.getVotes() + 1);
                        possibleCandidateRepository.save(p);
                    } else {
                        PossibleCandidate possibleCandidate = new PossibleCandidate(posCandidate.getName(), categoryRepository.findById(index).get());
                        possibleCandidateRepository.save(possibleCandidate);
                    }
                }
                index++;
            }
            tableAction.updateCandidatesubmit_status(name, voterRepository);
            return "candidateAddingSuccessful.html";
        }
    }

    @RequestMapping("/processVote")
    public String ProcessVote(@RequestParam String name, @RequestParam String voteValues) {
        if (voterRepository.findByEmail(name).getCandidatesubmit_status()) {
            return "errors/alreadySubmitted.html";
        } else {
            String[] partVoteValues = voteValues.split(",");
            for (String s : partVoteValues) {
                tableAction.voteFor(s, candidateRepository);
            }
            tableAction.updateVotingStatus(name, voterRepository);
            LOGGER.info(name + " has voted!");
            return "voteSuccessful.html";
        }
    }

    @RequestMapping("/dashboard")
    public String AccessDashboard(@RequestParam String password, Model model) {
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
