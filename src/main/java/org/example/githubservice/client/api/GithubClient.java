package org.example.githubservice.client.api;

import org.example.githubservice.model.Branch;
import org.example.githubservice.model.Repository;
import reactor.core.publisher.Flux;

public interface GithubClient {
    Flux<Repository> getAllRepositoriesByUser(String username);

    Flux<Branch> getAllBranches(Repository repository);
}
