package com.mettl.profiler.service;


import com.mettl.profiler.dao.CandidateData;
import com.mettl.profiler.dao.repository.CandidateDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    @Autowired
    private CandidateDataRepository candidateDataRepository;

    public CandidateData getCandidate(Integer id){
        return candidateDataRepository.findById(id).get();
    }

    public void createCandidate(CandidateData candidate){
        candidateDataRepository.save(candidate);
    }

    public CandidateData getCandidate(String email){
        return new CandidateData();
    }




}
