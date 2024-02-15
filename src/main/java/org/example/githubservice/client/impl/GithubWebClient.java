package org.example.githubservice.client.impl;

import org.example.githubservice.client.api.GithubClient;
import org.example.githubservice.model.Branch;
import org.example.githubservice.model.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GithubWebClient implements GithubClient {
    public static final String REPOS_OF_USER = "/users/{username}/repos";
    private final WebClient webClient;

    public GithubWebClient(WebClient.Builder webClientBuilder, @Value("${github.api.base-url}") String rootUrl) {
        this.webClient = webClientBuilder.baseUrl(rootUrl).build();
    }

    @Override
    public Flux<Repository> getAllRepositoriesByUser(String username) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(REPOS_OF_USER)
                        .build(username))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")))
                .bodyToFlux(Repository.class);
    }

    @Override
    public Flux<Branch> getAllBranches(Repository repository) {
        String uri = repository.branchesUrl().replace("{/branch}", "");
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Branch.class);
    }

}
