package com.mettl.profiler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jayway.jsonpath.JsonPath;
import com.mettl.profiler.dao.CandidateData;
import com.mettl.profiler.dao.CandidateProfile;
import com.mettl.profiler.service.CandidateProfileService;
import com.mettl.profiler.service.CandidateService;

@RestController
public class CandidateProfiler {

    Logger log = LogManager.getRootLogger();

    @Autowired
    public CandidateService candidateService;

    @Autowired
    public CandidateProfileService candidateProfileService;

    // get Version
    @RequestMapping("/version")
    public String getVersion() {
        log.info("Received request /version");
        return "{\"version\":\"1.0\"}";
    }

    // Get Candidate Data by ID
    @RequestMapping(method = RequestMethod.GET, value = "/getCandidate/{id}")
    public CandidateData getCandidate(@PathVariable String id) {
        return candidateService.getCandidate(Integer.parseInt(id));
    }

    // Create candidate and then create Profile
    @Async
    @RequestMapping(method = RequestMethod.POST, value = "/createProfile")
    public String createProfile(@RequestBody String crfJson) {

        CandidateData candidateData = new CandidateData();

        candidateData.setfirstName(JsonPath.read(crfJson, "$.name"));
        candidateData.setEmail(JsonPath.read(crfJson, "$.email"));
        candidateData.setCrfJson("{\"location\":\"" + JsonPath.read(crfJson, "$.name")
                + "\",\"lastName\":\"" + JsonPath.read(crfJson, "$.name") + "\"}");
        candidateService.createCandidate(candidateData);

        CandidateProfile candidateProfile = new CandidateProfile();
        candidateProfile.setGithub_json(
                "{\"EVENT_TYPE\":\"startAssessment\",\"invitation_key\":\"6a283287\"}");

        candidateProfile.setLinkedIn_json(
                "{\"EVENT_TYPE\":\"startAssessment\",\"invitation_key\":\"6a283287\"}");
        candidateProfile
                .setSo_json("{\"EVENT_TYPE\":\"startAssessment\",\"invitation_key\":\"6a283287\"}");
        candidateProfileService.createCandidateProfile(candidateProfile);

        return "asdasdgf";

    }

    // Get Profile by ID
    @RequestMapping(method = RequestMethod.GET, value = "/getProfile/{id}")
    public CandidateProfile getProfile(@PathVariable Integer id) {
        return candidateProfileService.getCandidateProfile("sdsad");
    }

    // Get Profile by email
    @RequestMapping(method = RequestMethod.GET, value = "/getProfile/{email}")
    public CandidateProfile getProfile(@PathVariable String email) {
        return candidateProfileService.getCandidateProfile("sdsad");
    }


}
