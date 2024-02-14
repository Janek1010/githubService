package org.example.githubservice.model.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

/**
 * Represents a customized error response for user not found.
 */
@Data
@Builder
public class UserNotFoundErrorResponseDTO {
    /**
     * HTTP status code of the error response.
     */
    private HttpStatusCode status;

    /**
     * Error message for user not found.
     */
    private String message;
}
