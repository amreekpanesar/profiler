package com.mettl.profiler.service;

import com.mettl.profiler.dao.CandidateProfile;
import com.mettl.profiler.dao.CandidateProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CandidateProfileService {



    @Autowired
    private CandidateProfileRepository candidateProfileTable;



    public CandidateProfile getCandidateProfile(String email){
        return new CandidateProfile();
    }

    public List<CandidateProfile> getAllCandidateProfiles(){
        List<CandidateProfile> candidateProfileList = new ArrayList<>();
//        candidateProfileTable.findAll()
//                .forEach(candidateProfileList::add);
        return candidateProfileList;
    }




}
