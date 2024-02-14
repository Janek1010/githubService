package org.example.githubservice.service.api;

import org.example.githubservice.model.dtos.RepositoryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for interacting with the GitHub API.
 */
public interface GithubService {
    /**
     * Retrieves a list of all repositories owned by the specified user.
     *
     * @param username GitHub username of the user.
     * @param page     Page number to be returned.
     * @param perPage  Number of records per page.
     * @return A flux of RepositoryDTO representing the repositories.
     */
    Flux<RepositoryDTO> listAllRepositoriesOfUser(String username, int page, int perPage);

    /**
     * Retrieves a list of all branches for the specified repository.
     *
     * @param repositoryDTO The RepositoryDTO for which to retrieve branches.
     * @return A mono of RepositoryDTO representing the repository with branches.
     */
    Mono<RepositoryDTO> listAllBranches(RepositoryDTO repositoryDTO);
}
