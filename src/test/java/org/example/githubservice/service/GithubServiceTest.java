package org.example.githubservice.service;

import org.example.githubservice.client.api.GithubClient;
import org.example.githubservice.model.Branch;
import org.example.githubservice.model.Commit;
import org.example.githubservice.model.Owner;
import org.example.githubservice.model.Repository;
import org.example.githubservice.model.dtos.BranchDTO;
import org.example.githubservice.model.dtos.RepositoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class GithubServiceTest {
    @Mock
    private GithubClient githubClient;

    @InjectMocks
    private GithubService githubService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetListOfBranchesDTO() {
        Repository repositoryTested = new Repository("Repo1", new Owner("User1"), false, "xyz.com", null);
        Branch branch1 = new Branch("branch1", new Commit("123"));
        Branch branch2 = new Branch("branch2", new Commit("321"));

        when(githubClient.getAllBranches(any())).thenReturn(Flux.just(branch1, branch2));

        Flux<BranchDTO> branchesReturned = githubService.getBranchesByRepository(repositoryTested);

        branchesReturned.collectList().block().forEach(branchDTO -> {
            if (branchDTO.name().equals("branch1")) {
                assertEquals(branch1.name(), branchDTO.name());
                assertEquals(branch1.commit().sha(), branchDTO.sha());
            } else if (branchDTO.name().equals("branch2")) {
                assertEquals(branch2.name(), branchDTO.name());
                assertEquals(branch2.commit().sha(), branchDTO.sha());
            }
        });

    }

    @Test
    void testGetRepositoriesWithBranchesByUser() {
        // given
        final String username = "maciek";
        List<Branch> branches1 = List.of(new Branch("Branch1", new Commit("123")), new Branch("Branch2", new Commit("321")));
        List<Branch> branches2 = List.of(new Branch("main", new Commit("456")), new Branch("master", new Commit("654")));

        Repository repository1 = new Repository("Repo1", new Owner(username), false, "xyz.com", branches1);
        Repository repository2 = new Repository("Repo2", new Owner(username), false, "xyz.com", branches2);

        // when

        when(githubClient.getAllRepositoriesByUser(username)).thenReturn(Flux.just(repository1, repository2));
        when(githubClient.getAllBranches(repository1)).thenReturn(Flux.fromIterable(branches1));
        when(githubClient.getAllBranches(repository2)).thenReturn(Flux.fromIterable(branches2));

        // then
        Flux<RepositoryDTO> result = githubService.getRepositoriesWithBranchesByUser(username);

        StepVerifier.create(result)
                .assertNext(dto -> {
                    assertEquals(dto.name(), repository1.name());
                    assertEquals(dto.ownerLogin(), repository1.owner().login());
                    assertEquals(dto.branches().size(), branches1.size());
                    for (int i = 0; i < dto.branches().size(); i++) {
                        assertEquals(dto.branches().get(i).name(), branches1.get(i).name());
                        assertEquals(dto.branches().get(i).sha(), branches1.get(i).commit().sha());
                    }
                })
                .assertNext(dto -> {
                    assertEquals(dto.name(), repository2.name());
                    assertEquals(dto.ownerLogin(), repository2.owner().login());
                    assertEquals(dto.branches().size(), branches2.size());
                    for (int i = 0; i < dto.branches().size(); i++) {
                        assertEquals(dto.branches().get(i).name(), branches2.get(i).name());
                        assertEquals(dto.branches().get(i).sha(), branches2.get(i).commit().sha());
                    }
                })
                .verifyComplete();
    }
}