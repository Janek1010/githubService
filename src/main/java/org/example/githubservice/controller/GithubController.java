package org.example.githubservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.githubservice.model.RepositoryDTO;
import org.example.githubservice.service.GithubService;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class GithubController {
    private final String REPOS_OF_USER = "/api/v1/users/{username}/repos";
    private final GithubService githubService;

    @GetMapping(value =REPOS_OF_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RepositoryDTO> listAllRepositoriesOfUser(@PathVariable String username){
        return githubService.listAllRepositoriesOfUser(username);
    }

}