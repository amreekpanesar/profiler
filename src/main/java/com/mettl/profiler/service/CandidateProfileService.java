package com.mettl.profiler.service;

import com.mettl.profiler.dao.CandidateProfile;
import com.mettl.profiler.dao.repository.CandidateProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CandidateProfileService {

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    public CandidateProfile getCandidateProfile(String email){
        return new CandidateProfile();
    }

    public List<CandidateProfile> getAllCandidateProfiles(){
        List<CandidateProfile> candidateProfileList = new ArrayList<>();
//        candidateProfileTable.findAll()
//                .forEach(candidateProfileList::add);
        return candidateProfileList;
    }

    public void createCandidateProfile(){
        candidateProfileRepository.save(new CandidateProfile());
    }




}
