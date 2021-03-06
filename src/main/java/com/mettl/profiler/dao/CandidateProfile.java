package com.mettl.profiler.dao;

import javax.persistence.*;

@Entity
@Table(name = "candidate_profile")
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String candidate_id;
    private String linkedin_json;
    private String github_json;
    private String so_json;

    public CandidateProfile() {}

    public CandidateProfile(Integer id, String candidate_id, String linkedIn_json,
            String github_json, String so_json) {
        this.id = id;
        this.candidate_id = candidate_id;
        this.linkedin_json = linkedIn_json;
        this.github_json = github_json;
        this.so_json = so_json;
    }

    public Integer getId() {
        return id;
    }

    public CandidateProfile setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCandidate_id() {
        return candidate_id;
    }

    public CandidateProfile setCandidate_id(String candidate_id) {
        this.candidate_id = candidate_id;
        return this;
    }

    public String getLinkedIn_json() {
        return linkedin_json;
    }

    public CandidateProfile setLinkedIn_json(String linkedIn_json) {
        this.linkedin_json = linkedIn_json;
        return this;
    }

    public String getGithub_json() {
        return github_json;
    }

    public CandidateProfile setGithub_json(String github_json) {
        this.github_json = github_json;
        return this;
    }

    public String getSo_json() {
        return so_json;
    }

    public CandidateProfile setSo_json(String so_json) {
        this.so_json = so_json;
        return this;
    }

}
