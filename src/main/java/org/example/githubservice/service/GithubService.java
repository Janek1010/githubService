package org.example.githubservice.service;

import org.example.githubservice.client.api.GithubClient;
import org.example.githubservice.model.Repository;
import org.example.githubservice.model.dtos.BranchDTO;
import org.example.githubservice.model.dtos.RepositoryDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GithubService {
    private final GithubClient githubClient;

    public GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public Flux<RepositoryDTO> getRepositoriesWithBranchesByUser(String username) {
        return githubClient.getAllRepositoriesByUser(username)
                .filter(repositoryDTO -> !repositoryDTO.fork())
                .flatMap(repository -> {
                    new RepositoryDTO(repository.name(), repository.owner().login(), getListOfBranchesDTO(repository))

                });

    }

    private Mono<List<BranchDTO>> getListOfBranchesDTO(Repository repository) {
        return githubClient.getAllBranches(repository)
                .map(branch -> new BranchDTO(branch.name(), branch.commit().sha()))
                .collectList();
    }
}
