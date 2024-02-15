package org.example.githubservice.controller;

import org.example.githubservice.model.dtos.RepositoryDTO;
import org.example.githubservice.model.dtos.UserNotFoundErrorResponseDTO;
import org.example.githubservice.service.GithubService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@RestController
public class GithubController {
    private final String REPOS_OF_USER = "/api/v1/users/{username}/repos";
    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping(value = REPOS_OF_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RepositoryDTO> listAllRepositoriesOfUser(@PathVariable String username) {
        return githubService.getRepositoriesWithBranchesByUser(username);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<UserNotFoundErrorResponseDTO> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(new UserNotFoundErrorResponseDTO(ex.getStatusCode(), ex.getReason()));
    }
}
