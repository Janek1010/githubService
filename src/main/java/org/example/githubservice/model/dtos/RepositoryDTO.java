package org.example.githubservice.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RepositoryDTO {
    private String name;

    @JsonProperty("ownerLogin")
    private OwnerDTO owner;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean fork;

    @JsonProperty(value = "branches_url", access = JsonProperty.Access.WRITE_ONLY)
    private String branchesUrl;

    private List<BranchDTO> branches;
}
