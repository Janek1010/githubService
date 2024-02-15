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

@Service
@Primary
public class GithubServiceImpl implements GithubService {
    private final String REPOS_OF_USER = "/users/{username}/repos";
    private final WebClient webClient;

    public GithubServiceImpl() {
        this.webClient = WebClient.create("https://api.github.com");
    }

    @Override
    public Flux<RepositoryDTO> listAllRepositoriesOfUser(String username) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(REPOS_OF_USER)
                        .build(username))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")))
                .bodyToFlux(RepositoryDTO.class)
                .filter(repositoryDTO -> !repositoryDTO.isFork())
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
                .map(branches -> {
                    repositoryDTO.setBranches(branches);
                    return repositoryDTO;
                });
    }

}
