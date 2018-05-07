package com.mettl.profiler.service;

import com.jayway.jsonpath.JsonPath;
import com.mettl.profiler.crawler.Crawler;
import com.mettl.profiler.dao.CandidateData;
import com.mettl.profiler.dao.CandidateProfile;
import com.mettl.profiler.dao.repository.CandidateProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateProfileService {

    Crawler crawler = new Crawler();

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    @Autowired
    public CandidateService candidateService;

    public CandidateProfile getCandidateProfile(String email) {
        //TODO
        return new CandidateProfile();
    }

    public CandidateProfile getCandidateProfile(Integer id) {
        return candidateProfileRepository.findById(id).get();
    }

    public List<CandidateProfile> getAllCandidateProfiles() {
        List<CandidateProfile> candidateProfileList = new ArrayList<>();
        // candidateProfileTable.findAll()
        // .forEach(candidateProfileList::add);
        return candidateProfileList;
    }

    @Async("syncTaskExecutor")
    public void createCandidateProfile(CandidateData candidateData) {

        candidateService.createCandidate(candidateData);

        String crfJson = candidateData.getCrfJson();

        CandidateProfile candidateProfile = new CandidateProfile();

        candidateProfile.setCandidate_id(candidateData.getEmail());
        candidateProfile.setGithub_json(
                crawler.getGitHubData(
                        candidateData.getfirstName(),
                        JsonPath.read(crfJson, "$.lastName"),
                        JsonPath.read(crfJson, "$.location"),
                        JsonPath.read(crfJson, "$.organization"),
                        candidateData.getEmail())
        );
        candidateProfile.setLinkedIn_json(
                crawler.getGitHubData(
                        candidateData.getfirstName(),
                        JsonPath.read(crfJson, "$.lastName"),
                        JsonPath.read(crfJson, "$.location"),
                        JsonPath.read(crfJson, "$.organization"),
                        candidateData.getEmail()));

        candidateProfile.setSo_json(
                crawler.getStackOverflowProfile(
                        candidateData.getfirstName(),
                        JsonPath.read(crfJson, "$.lastName"),
                        JsonPath.read(crfJson, "$.location"),
                        JsonPath.read(crfJson, "$.organization"),
                        candidateData.getEmail())
        );
        candidateProfileRepository.save(candidateProfile);
    }
}
