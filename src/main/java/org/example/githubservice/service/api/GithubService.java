package org.example.githubservice.service.api;

import org.example.githubservice.model.dtos.RepositoryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GithubService {
    Flux<RepositoryDTO> listAllRepositoriesOfUser(String username, int page, int perPage);
    Mono<RepositoryDTO> listAllBranches(RepositoryDTO repositoryDTO);
}
