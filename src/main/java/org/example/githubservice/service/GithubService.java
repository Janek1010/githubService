package org.example.githubservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.githubservice.model.RepositoryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GithubService {
    Flux<RepositoryDTO> listAllRepositoriesOfUser(String username, int page, int perPage);
    Mono<RepositoryDTO> listAllBranches(RepositoryDTO repositoryDTO);
}
