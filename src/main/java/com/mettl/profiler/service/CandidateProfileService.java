package com.mettl.profiler.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mettl.profiler.dao.CandidateProfile;
import com.mettl.profiler.dao.repository.CandidateProfileRepository;

@Service
public class CandidateProfileService {

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    public CandidateProfile getCandidateProfile(String email) {
        return new CandidateProfile();
    }

    public List<CandidateProfile> getAllCandidateProfiles() {
        List<CandidateProfile> candidateProfileList = new ArrayList<>();
        // candidateProfileTable.findAll()
        // .forEach(candidateProfileList::add);
        return candidateProfileList;
    }

    public void createCandidateProfile(CandidateProfile candidateProfile) {
        candidateProfileRepository.save(candidateProfile);
    }



}
