package org.example.githubservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RepositoryDTO {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean fork;
    private String name;
    private OwnerDTO owner;

    @JsonProperty("branches_url")
    private String branchesUrl;
    private List<BranchDTO> branches;

}
