package org.example.githubservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.githubservice.model.RepositoryDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user
 */
@Service
@Primary
public class GithubServiceImpl implements GithubService{
    private final String REPOS_OF_USER = "/users/{username}/repos";
    private final WebClient webClient;

    public GithubServiceImpl() {
        this.webClient = WebClient.create("https://api.github.com");
    }

    @Override
    public Flux<RepositoryDTO> listAllRepositoriesOfUser(String username) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(REPOS_OF_USER).build(username))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(RepositoryDTO.class)
                .filter(repositoryDTO -> !repositoryDTO.isFork());
    }
}
