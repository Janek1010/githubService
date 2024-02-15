package org.example.githubservice.model.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public record OwnerDTO (
        @JsonValue
        @JsonProperty("login")
        String login
) {}
