package org.example.githubservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryDTO {
    private boolean fork;
    private String name;
    private OwnerDTO owner;
    @JsonProperty("branches_url")
    private String branchesUrl;
}
