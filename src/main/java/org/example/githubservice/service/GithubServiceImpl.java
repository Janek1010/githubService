package org.example.githubservice.service;

import org.example.githubservice.model.RepositoryDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user
 */
@Service
public class GithubServiceImpl implements GithubService{
    private final WebClient webClient;

    public GithubServiceImpl() {
        this.webClient = WebClient.create("https://api.github.com");
    }

    @Override
    public Flux<RepositoryDTO> listAllRepositoriesOfUser(String username) {

        return null;
    }
}
