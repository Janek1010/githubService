package org.example.githubservice.service;

import org.example.githubservice.model.RepositoryDTO;
import reactor.core.publisher.Flux;

public interface GithubService {
    Flux<RepositoryDTO> listAllRepositoriesOfUser(String username);
}
