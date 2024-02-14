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

/**
 * Class that handles the requests to the Github API.
 */
@RestController
@RequiredArgsConstructor
public class GithubController {
    private final String REPOS_OF_USER = "/api/v1/users/{username}/repos";
    private final GithubService githubService;

    /**
     * Retrieves a list of all repositories owned by the specified GitHub user.
     * This version of the API does not require authorization, allowing up to 60 requests per hour.
     *
     * @param username GitHub username.
     * @param page     Page number to be returned.
     * @param perPage  Number of records per page; GitHub handles a maximum of 100.
     * @return A stream of repositories in JSON format, including owner login, repository name, and branches.
     * Returns 404 if the user is not found, 200 OK if the user is found.
     * @see <a href="https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user">documentation of endpoint</a>
     */
    @GetMapping(value = REPOS_OF_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RepositoryDTO> listAllRepositoriesOfUser(@PathVariable String username,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "30") int perPage) {
        return githubService.listAllRepositoriesOfUser(username, page, perPage);
    }

    /**
     * The method is responsible for returning the error message in a different format.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<UserNotFoundErrorResponseDTO> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(UserNotFoundErrorResponseDTO
                .builder().message(ex.getReason()).status(ex.getStatusCode())
                .build());
    }
}
