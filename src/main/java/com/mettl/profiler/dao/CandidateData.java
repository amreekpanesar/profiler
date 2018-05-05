package com.mettl.profiler.dao;

import javax.persistence.*;

@Entity
public class CandidateData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String email;
    private String firstName;
    private String crfJson;

    public CandidateData() {
    }

    public CandidateData(Integer id, String email, String firstName, String crfJson) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.crfJson = crfJson;
    }

    public Integer getId() {
        return id;
    }

    public CandidateData setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CandidateData setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getfirstName() {
        return firstName;
    }

    public CandidateData setfirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getCrfJson() {
        return crfJson;
    }

    public CandidateData setCrfJson(String crfJson) {
        this.crfJson = crfJson;
        return this;
    }
}
