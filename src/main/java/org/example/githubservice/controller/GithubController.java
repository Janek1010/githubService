package org.example.githubservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.githubservice.model.dtos.RepositoryDTO;
import org.example.githubservice.model.dtos.UserNotFoundErrorResponseDTO;
import org.example.githubservice.service.api.GithubService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class GithubController {
    private final String REPOS_OF_USER = "/api/v1/users/{username}/repos";
    private final GithubService githubService;

    @GetMapping(value = REPOS_OF_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RepositoryDTO> listAllRepositoriesOfUser(@PathVariable String username) {
        return githubService.listAllRepositoriesOfUser(username);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<UserNotFoundErrorResponseDTO> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(UserNotFoundErrorResponseDTO
                .builder().message(ex.getReason()).status(ex.getStatusCode())
                .build());
    }
}
