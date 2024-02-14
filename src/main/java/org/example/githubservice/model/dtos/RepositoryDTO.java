package org.example.githubservice.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents a repository on GitHub.
 */
@Data
@Builder
public class RepositoryDTO {
    /**
     * Name of the repository.
     */
    private String name;

    /**
     * Owner of the repository. In the response there will be presented value 'login' from OwnerDTO as ownerLogin
     */
    @JsonProperty("ownerLogin")
    private OwnerDTO owner;

    /**
     * Indicates if the repository is a fork. Invisible in a response
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean fork;

    /**
     * URL to list branches of the repository. Invisible in a response
     */
    @JsonProperty(value = "branches_url", access = JsonProperty.Access.WRITE_ONLY)
    private String branchesUrl;

    /**
     * List of branches of the repository.
     */
    private List<BranchDTO> branches;
}
