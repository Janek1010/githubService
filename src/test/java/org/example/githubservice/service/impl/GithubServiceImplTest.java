package org.example.githubservice.service.impl;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.githubservice.model.dtos.BranchDTO;
import org.example.githubservice.model.dtos.CommitDTO;
import org.example.githubservice.model.dtos.OwnerDTO;
import org.example.githubservice.model.dtos.RepositoryDTO;
import org.example.githubservice.service.api.GithubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WireMockTest(httpPort = 8081)
@ExtendWith(SpringExtension.class)
class GithubServiceImplTest {
    @Autowired
    GithubService githubService;
    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("github.api.base-url", () -> "http://localhost:8081");
    }

    @Test
    void should_return_correct_repositories_Flux() throws IOException {
        // given
        String responseBodyRepositories = IOUtils.resourceToString("/files/correct-response-repositories-from-githubAPI.json", StandardCharsets.UTF_8);
        String responseBodyBranches = IOUtils.resourceToString("/files/correct-response-branches-from-githubAPI.json", StandardCharsets.UTF_8);

        BranchDTO branchDTO = new BranchDTO("main", new CommitDTO("7155aa7c2d68f7e8ab38abbed9ea22595441b32a"));
        RepositoryDTO repositoryTested = new RepositoryDTO("gimmemoji", new OwnerDTO("jotzet"), new LinkedList<>(List.of(branchDTO)));

        String username = "jotzet";

        stubFor(get(urlEqualTo("/users/{username}/repos".replace("{username}", username)))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBodyRepositories)
                )
        );

        stubFor(get(urlEqualTo("/repos/{username}/{repository}/branches".replace("{username}", username).replace("{repository}", repositoryTested.name())))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBodyBranches)
                )
        );
        //when
        Flux<RepositoryDTO> response = githubService.listAllRepositoriesOfUser("jotzet");

        // then
        response.doOnNext(repositoryDTO -> {
            assertThat(repositoryDTO.name()).isEqualTo(repositoryTested.name());
            assertThat(repositoryDTO.owner()).isEqualTo(repositoryTested.owner());
            assertThat(repositoryDTO.branches().getFirst().name()).isEqualTo(repositoryTested.branches().getFirst().name());
            assertThat(repositoryDTO.branches().getFirst().commit().sha()).isEqualTo(repositoryTested.branches().getFirst().commit().sha());
        }).blockLast();
    }

}