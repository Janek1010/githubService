package org.example.githubservice.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the owner of a repository.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDTO {
    @JsonValue
    @JsonProperty("login")
    private String login;
}
