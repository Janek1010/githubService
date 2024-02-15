package org.example.githubservice.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record RepositoryDTO(
        String name,
        //@JsonProperty("ownerLogin")
        OwnerDTO owner,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        boolean fork,
        @JsonProperty(value = "branches_url", access = JsonProperty.Access.WRITE_ONLY)
        String branchesUrl,
        List<BranchDTO> branches
) {}
