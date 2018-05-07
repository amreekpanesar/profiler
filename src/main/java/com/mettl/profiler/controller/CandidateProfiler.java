package com.mettl.profiler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jayway.jsonpath.JsonPath;
import com.mettl.profiler.crawler.Crawler;
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
    @RequestMapping(method = RequestMethod.POST, value = "/createProfile")
    public String createProfile(@RequestBody String crfJson) {

        CandidateData candidateData = new CandidateData();

        String firstName = JsonPath.read(crfJson, "$.name");
        String email = JsonPath.read(crfJson, "$.email");
        String lastName = JsonPath.read(crfJson, "$.lastName");
        String location = JsonPath.read(crfJson, "$.location");

        candidateData.setfirstName(firstName);
        candidateData.setEmail(email);
        candidateData.setCrfJson(
                "{\"location\":\"" + location + "\",\"lastName\":\"" + lastName + "\"}");

        candidateProfileService.createCandidateProfile(candidateData);
        return "{\"success\":true}";
    }

    // Get Profile by ID
    @RequestMapping(method = RequestMethod.GET, value = "/getProfile/id/{id}")
    public CandidateProfile getProfile(@PathVariable int id) {
        return candidateProfileService.getCandidateProfile(id);
    }

    // Get Profile by email
    @RequestMapping(method = RequestMethod.GET, value = "/getProfile/email/{email}")
    public CandidateProfile getProfile( String email) {
        return candidateProfileService.getCandidateProfile("sdsad");
    }

}
