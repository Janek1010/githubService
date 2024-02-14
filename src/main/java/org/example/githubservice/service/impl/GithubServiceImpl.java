package org.example.githubservice.service.impl;

import org.example.githubservice.model.dtos.BranchDTO;
import org.example.githubservice.model.dtos.RepositoryDTO;
import org.example.githubservice.service.api.GithubService;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the GithubService interface for interacting with the GitHub API.
 */
@Service
@Primary
public class GithubServiceImpl implements GithubService {
    private final String REPOS_OF_USER = "/users/{username}/repos";
    private final WebClient webClient;

    public GithubServiceImpl() {
        this.webClient = WebClient.create("https://api.github.com");
    }

    @Override
    public Flux<RepositoryDTO> listAllRepositoriesOfUser(String username, int page, int perPage) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(REPOS_OF_USER)
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build(username))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                // Handles response statuses, in this case 404 (NOT_FOUND).
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")))
                // Maps the response body to a Flux<RepositoryDTO> stream.
                .bodyToFlux(RepositoryDTO.class)
                .filter(repositoryDTO -> !repositoryDTO.isFork())
                // For each repository, calls the listAllBranches method to retrieve a list of branches.
                .flatMap(this::listAllBranches);
    }

    @Override
    public Mono<RepositoryDTO> listAllBranches(RepositoryDTO repositoryDTO) {
        String uri = repositoryDTO.getBranchesUrl().replace("{/branch}", "");
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(BranchDTO.class)
                .collectList()
                // Collected branches are mapped to a list and assigned to the repository.
                .map(branches -> {
                    repositoryDTO.setBranches(branches);
                    return repositoryDTO;
                });
    }

}
