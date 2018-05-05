package com.mettl.profiler.controller;

import com.mettl.profiler.dao.CandidateData;
import com.mettl.profiler.dao.CandidateProfile;
import com.mettl.profiler.service.CandidateProfileService;
import com.mettl.profiler.service.CandidateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@RestController
public class CandidateProfiler {

    Logger log = LogManager.getRootLogger();

    @Autowired
    public CandidateService candidateService;

    @Autowired
    public CandidateProfileService candidateProfileService;

    //get Version
    @RequestMapping("/version")
    public String getVersion(){
        log.info("Received request /version");
        return "{\"version\":\"1.0\"}";
    }

    // Get Candidate Data by ID
    @RequestMapping(method = RequestMethod.GET, value="/getCandidate/{id}")
    public CandidateData createProfile(@PathVariable String id){
        return candidateService.getCandidate(Integer.parseInt(id));
    }

    // Create candidate and then create Profile
    @Async
    @RequestMapping(method = RequestMethod.POST, value="/createProfile")
    public CandidateData createProfile(@RequestBody CandidateData crfJson){
        candidateService.createCandidate(crfJson);
        //candidateProfileService
        return candidateService.getCandidate(1);
    }

    // Get Profile by ID
    @RequestMapping(method = RequestMethod.GET, value="/getProfile/{id}")
    public CandidateProfile getProfile(@PathVariable Integer id){
        return candidateProfileService.getCandidateProfile("sdsad");
    }

    // Get Profile by email
    @RequestMapping(method = RequestMethod.GET, value="/getProfile/{email}")
    public CandidateProfile getProfile(@PathVariable String email){
        return candidateProfileService.getCandidateProfile("sdsad");
    }


}
