package org.example.githubservice.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record RepositoryDTO(
        String name,
        //@JsonProperty("ownerLogin")
        OwnerDTO owner,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        Boolean fork,
        @JsonProperty(value = "branches_url", access = JsonProperty.Access.WRITE_ONLY)
        String branchesUrl,
        List<BranchDTO> branches
) {
        public RepositoryDTO(String name, OwnerDTO owner, List<BranchDTO> branches) {
                this(name, owner,null, null, branches);
        }
}
