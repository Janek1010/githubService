package org.example.githubservice.model.dtos;

import org.springframework.http.HttpStatusCode;

public record UserNotFoundErrorResponseDTO (
        HttpStatusCode status,
        String message
) {}
