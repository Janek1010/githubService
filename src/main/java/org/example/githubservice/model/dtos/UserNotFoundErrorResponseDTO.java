package org.example.githubservice.model.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
@Builder
public class UserNotFoundErrorResponseDTO {

    private HttpStatusCode status;

    private String message;
}
