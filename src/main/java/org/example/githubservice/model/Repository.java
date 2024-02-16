package org.example.githubservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Repository(
        String name,
        Owner owner,
        boolean fork,
        @JsonProperty("branches_url")
        String branchesUrl,
        List<Branch> branches
) {
}
