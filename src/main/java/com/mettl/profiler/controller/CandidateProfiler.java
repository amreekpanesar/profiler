package com.mettl.profiler.controller;

import com.mettl.profiler.dao.CandidateData;
import com.mettl.profiler.service.CandidateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CandidateProfiler {


    Logger log = LogManager.getRootLogger();

    @Autowired
    public CandidateService candidateService;

    @RequestMapping("/version")
    public String getVersion(){
        log.debug("Receiver request /version");
        return "{\"version\":\"1.0\"}";
    }

    @RequestMapping(method = RequestMethod.POST, value="/createProfile")
    public CandidateData createProfile(@RequestBody CandidateData crfJson){
        candidateService.createCandidate(crfJson);
        return candidateService.getCandidate(1);
    }

    @RequestMapping(method = RequestMethod.GET, value="/getCandidate/{id}")
    public CandidateData createProfile(@RequestParam Integer id){
        return candidateService.getCandidate(id);
    }

    @RequestMapping("/getProfile/{id}")
    public CandidateData getProfile(@PathVariable Integer id){
        return new CandidateData(1,"23123","23123", "2312312");
    }

    @RequestMapping("/getProfile/{email}")
    public CandidateData getProfile(@PathVariable String email){
        return new CandidateData(1,"23123","23123", "2312312");
    }


}
