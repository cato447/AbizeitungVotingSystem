package com.github.cato447.AbizeitungVotingSystem.controller;

import com.github.cato447.AbizeitungVotingSystem.entities.*;
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
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Controller
public class VotingController {

    private boolean votingPhase;
    private boolean addingPhase;

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
            votingPhase = false;
            addingPhase = true;

            LOGGER.info("Program started with arguments: votingPhase="+ votingPhase + " addingPhase=" + addingPhase);

            if (voterRepository.findAll().size() == 0) {
                tableAction.setUpVoters(voterRepository);
                LOGGER.info("Voters successfully set up");
            }

            if (categoryRepository.findAll().size() == 0) {
                tableAction.setUpCategories(categoryRepository);
                possibleCandidateRepository.deleteAll();
                LOGGER.info("Categories successfully set up");
            }

            if (candidateRepository.findAll().size() == 0 && votingPhase == true && possibleCandidateRepository.findAll().size() != 0) {
                tableAction.setUpCandidates(possibleCandidateRepository, candidateRepository);
                LOGGER.info("Candidates successfully set up");
            }
        }

    @RequestMapping("/")
    public String WelcomeSite() {
        LocalDate finishDate = LocalDate.of(2021, Month.JANUARY,17);
        LocalDate now = LocalDate.now();

        if(now.isAfter(finishDate)){
            LOGGER.warn("passed");
            return "errors/votingClosed.html";
        } else {
            LOGGER.warn("in Bounds");
            return "start.html";
        }
    }

    public void sendSimpleMessage(String to, String subject, String text) {
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
                if (voter == null){
                    LOGGER.error(name + " is not allowed to vote");
                    return "errors/notRegistered.html";
                }
                if (voter.getVote_status() && votingPhase) {
                    LOGGER.warn(name + " has already voted");
                    return "errors/alreadyVoted.html";
                } else if (voter.getCandidatesubmit_status() && addingPhase) {
                    LOGGER.warn(name + " has already submitted its candidates");
                    return "errors/alreadyVoted.html";
                } else {
                    if (authCodesRepository.findByName(name) == null) {
                        AuthCode authCode = tableAction.generateToken(name, RandomNumber.getRandomNumberString(), authCodesRepository);
                        sendSimpleMessage(name, "Code zur Authentifizierung", "Dein Code lautet: " + authCode.getCode());
                    } else if (authCodesRepository.findByName(name) != null && authCodesRepository.findByName(name).isExpired()) {
                        AuthCode authCode = tableAction.generateToken(name, RandomNumber.getRandomNumberString(), authCodesRepository);
                        sendSimpleMessage(name, "Code zur Authentifizierung", "Dein Code lautet: " + authCode.getCode());
                    }
                    model.addAttribute("name", name);
                    model.addAttribute("codeTime", authCodesRepository.findByName(name).getExpirationTime());
                    model.addAttribute("codeExpired", false);
                    model.addAttribute("codeFalse", false);
                    return "authenticate.html";
                }
            } catch (Exception e) {
                e.printStackTrace();
                tableAction.deleteToken(name, authCodesRepository);
                return "error.html";
            }
        } else {
            return "errors/falseInput.html";
        }
    }

    @RequestMapping("/newCode")
    public String provideNewCode(@RequestParam String name, Model model){
        AuthCode authCode = tableAction.generateToken(name, RandomNumber.getRandomNumberString(), authCodesRepository);
        sendSimpleMessage(name, "Code zur Authentifizierung", "Dein Code lautet: " + authCode.getCode());
        model.addAttribute("name", name);
        model.addAttribute("codeTime", authCodesRepository.findByName(name).getExpirationTime());
        model.addAttribute("codeExpired", false);
        model.addAttribute("codeFalse", false);
        return "authenticate.html";
    }

    @RequestMapping("/vote")
    public String voting_adding(@RequestParam String code, @RequestParam String name, Model model) {
        String tokenStatus = tableAction.checkToken(name, code, authCodesRepository);
        if (tokenStatus.equals("matched")) {
            LOGGER.warn("matched");
            if (addingPhase) {
                PossibleCandidateWrapper possibleCandidates = new PossibleCandidateWrapper();
                List<Category> categories = categoryRepository.findAll();
                for (int i = 0; i < categories.size(); i++) {
                    possibleCandidates.addPossibleCandidate(new PossibleCandidate());
                }
                model.addAttribute("categories", categories);
                model.addAttribute("form", possibleCandidates);
                model.addAttribute("name", name);
                return "addingCandidates.html";
            } else if (votingPhase) {
                List<Category> categories = categoryRepository.findAll();
                model.addAttribute("categories", categories);
                model.addAttribute("name", name);
                return "voting.html";
            }
        } else if (tokenStatus.equals("expired")) {
            LOGGER.warn("expired");
            model.addAttribute("name", name);
            model.addAttribute("codeExpired", true);
            model.addAttribute("codeFalse", false);
            return "authenticate.html";
        } else if (tokenStatus.equals("wrong")) {
            LOGGER.warn("wrong");
            model.addAttribute("name", name);
            model.addAttribute("codeExpired", false);
            model.addAttribute("codeFalse", true);
            return "authenticate.html";
        }
        return "fatalError";
    }

    @RequestMapping("/saveCandidates")
    public String candidateSaving(@ModelAttribute PossibleCandidateWrapper possibleCandidates, @RequestParam String name) {
        if (voterRepository.findByEmail(name).getCandidatesubmit_status()) {
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
                    } else if (possibleCandidateRepository.findByNameAndCategory(posCandidate.getName().split(" ")[posCandidate.getName().split(" ").length-1], categoryRepository.findById(index).get()) != null){
                        PossibleCandidate p = possibleCandidateRepository.findByNameAndCategory(posCandidate.getName().split(" ")[posCandidate.getName().split(" ").length-1], categoryRepository.findById(index).get());
                        p.setVotes(p.getVotes() + 1);
                        possibleCandidateRepository.save(p);
                    } else {
                        if (index > 31 && posCandidate.getName().indexOf(" ") != -1) {
                            if (posCandidate.getName().split(" ")[posCandidate.getName().split(" ").length-1].equals("Neumann") ||
                                    posCandidate.getName().split(" ")[posCandidate.getName().split(" ").length-1].equals("neumann") ||
                                    posCandidate.getName().split(" ")[posCandidate.getName().split(" ").length-1].equals("Mecklenburg") ||
                                    posCandidate.getName().split(" ")[posCandidate.getName().split(" ").length-1].equals("mecklenburg")){
                                PossibleCandidate possibleCandidate = new PossibleCandidate(posCandidate.getName(), categoryRepository.findById(index).get());
                                possibleCandidateRepository.save(possibleCandidate);
                            } else {
                                posCandidate.setName(posCandidate.getName().split(" ")[posCandidate.getName().split(" ").length - 1]);
                                PossibleCandidate possibleCandidate = new PossibleCandidate(posCandidate.getName(), categoryRepository.findById(index).get());
                                possibleCandidateRepository.save(possibleCandidate);
                            }
                        } else {
                            PossibleCandidate possibleCandidate = new PossibleCandidate(posCandidate.getName(), categoryRepository.findById(index).get());
                            possibleCandidateRepository.save(possibleCandidate);
                        }
                    }
                }
                index++;
            }

            tableAction.updateCandidatesubmit_status(name, voterRepository);
            return "voteSuccessful.html";
        }
    }

    @RequestMapping("/processVote")
    public String ProcessVote(@RequestParam String name, @RequestParam String voteValues) {
        if (voterRepository.findByEmail(name).getVote_status()) {
            return "errors/alreadyVoted.html";
        } else {
            LinkedList<String> voteFor = new LinkedList<>();
            if(!voteValues.equals("")) {
                String[] partVoteValues = voteValues.split(",");
                for (String s : partVoteValues) {
                    voteFor.add(s);
                }
                LOGGER.info(name + " has voted!");
            }
            for (String s: voteFor) {
                tableAction.voteForCandidate(s, candidateRepository);
            }

            tableAction.updateVotingStatus(name, voterRepository);
            return "voteSuccessful.html";
        }
    }

}
