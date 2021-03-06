package com.mettl.profiler.controller;

import com.jayway.jsonpath.JsonPath;
import com.mettl.profiler.dao.CandidateData;
import com.mettl.profiler.dao.CandidateProfile;
import com.mettl.profiler.service.CandidateProfileService;
import com.mettl.profiler.service.CandidateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

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
    public ResponseEntity<CandidateData> getCandidate(@PathVariable String id) {
        CandidateData candidateData = null;
        try {
            candidateData = candidateService.getCandidate(Integer.parseInt(id));
            return new ResponseEntity<>(candidateData, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(candidateData, HttpStatus.NOT_FOUND);
        }
    }

    // Create candidate and then create Profile
    @RequestMapping(method = RequestMethod.POST, value = "/createProfile")
    public String createProfile(@RequestBody String crfJson) {
        try {
            CandidateData candidateData = new CandidateData();

            String firstName = "";
            String email = "";
            String lastName = "";
            String location = "";
            String organization = "";

            try {
                firstName = JsonPath.read(crfJson, "$.name");
                email = JsonPath.read(crfJson, "$.email");
                lastName = JsonPath.read(crfJson, "$.lastName");
                location = JsonPath.read(crfJson, "$.location");
                organization = JsonPath.read(crfJson, "$.organization");

            } catch (Exception e) {
                e.printStackTrace();
            }

            candidateData.setfirstName(firstName);
            candidateData.setEmail(email);
            candidateData.setCrfJson(
                    "{\"location\":\"" + location + "\", \"organization\":\"" + organization + "\" ,\"lastName\":\"" + lastName + "\"}");

            candidateProfileService.createCandidateProfile(candidateData);

            return "{\"success\":true}";
        } catch (Exception e) {
            return "{\"success\":false}";
        }
    }

    // Get Profile by ID
    @RequestMapping(method = RequestMethod.GET, value = "/getProfile/id/{id}")
    public ResponseEntity<CandidateProfile> getProfile(@PathVariable int id) {
        CandidateProfile candidateProfile = null;
        try {
            candidateProfile = candidateProfileService.getCandidateProfile(id);
            return new ResponseEntity<>(candidateProfile, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(candidateProfile, HttpStatus.NOT_FOUND);
        }
    }

    // Get Profile by email
    @RequestMapping(method = RequestMethod.GET, value = "/getProfile/email/{email}")
    public ResponseEntity<CandidateProfile> getProfile(@PathVariable String email) {
        CandidateProfile candidateProfile = null;
        try {
            candidateProfile = candidateProfileService.getCandidateProfile(email);
            return new ResponseEntity<>(candidateProfile, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(candidateProfile, HttpStatus.NOT_FOUND);
        }
    }
}
