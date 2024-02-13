package org.example.githubservice.model;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user
 */
@Service
public class GithubServiceImpl implements GithubService{
    private final WebClient webClient;

    public GithubServiceImpl() {
        this.webClient = WebClient.create("https://api.github.com");
    }
}
