package org.example.githubservice.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a commit in a repository.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommitDTO {

    /**
     * SHA of the  LAST commit.
     */
    @JsonValue
    @JsonProperty("sha")
    private String sha;

}