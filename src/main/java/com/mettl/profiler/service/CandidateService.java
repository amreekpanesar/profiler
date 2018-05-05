package com.mettl.profiler.service;


import com.mettl.profiler.dao.CandidateData;
import com.mettl.profiler.dao.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {


    @Autowired
    private CandidateRepository candidateRepository;

    public CandidateData getCandidate(Integer id){
        return new CandidateData();
    }

    public void createCandidate(CandidateData candidate){
        candidateRepository.save(candidate);
    }

    public CandidateData getCandidate(String email){
        return new CandidateData();
    }




}
