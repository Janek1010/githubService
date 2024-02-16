package org.example.githubservice.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.githubservice.client.api.GithubClient;
import org.example.githubservice.model.Branch;
import org.example.githubservice.model.Commit;
import org.example.githubservice.model.Owner;
import org.example.githubservice.model.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@WireMockTest // it will take random port, it includes also @ExtendWith(WireMockExtension.class)
class GithubWebClientTest {
    @Autowired
    GithubClient githubClient;

//    @Test
//    @Disabled
//    void testingRandomPortAndLocalhost(WireMockRuntimeInfo wmRuntimeInfo) {
//        int port = wmRuntimeInfo.getHttpPort();
//        System.out.println("Port: " + port);
//        System.out.println("Host: "+  wmRuntimeInfo.getHttpBaseUrl());
//    }

    @Test
    void shouldReturnCorrectRepositoriesFlux() throws IOException {
        // given
        final String responseBodyRepositories = IOUtils.resourceToString("/files/correct-response-repositories-from-githubAPI.json", StandardCharsets.UTF_8);
        Repository repositoryTested = new Repository("gimmemoji", new Owner("jotzet"), false, "https://api.github.com/repos/jotzet/gimmemoji/branches{/branch}", null);
        Repository repositoryTested2 = new Repository("pianoroll-frontend-challenge", new Owner("jotzet"), true, "https://api.github.com/repos/jotzet/pianoroll-frontend-challenge/branches{/branch}", null);

        stubFor(get(urlEqualTo("/users/{username}/repos".replace("{username}", repositoryTested.owner().login())))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBodyRepositories)
                )
        );

        //when
        Flux<Repository> response = githubClient.getAllRepositoriesByUser(repositoryTested.owner().login());

        // then
        StepVerifier.create(response)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(repository -> true)
                .consumeRecordedWith(repositories -> {
                    assertThat(repositories.size()).isEqualTo(2);
                    assertThat(repositories).containsExactlyInAnyOrder(repositoryTested, repositoryTested2);
                })
                .verifyComplete();
    }


    @Test
    void shouldReturnCorrectBranchesFlux() throws IOException {
        // given
        final String responseBodyBranches = IOUtils.resourceToString("/files/correct-response-branches-from-githubAPI.json", StandardCharsets.UTF_8);
        Branch branch = new Branch("main", new Commit("7155aa7c2d68f7e8ab38abbed9ea22595441b32a"));
        final String username = "jotzet";
        Repository repositoryTested = new Repository("gimmemoji", new Owner("jotzet"), false, "https://api.github.com/repos/jotzet/gimmemoji/branches{/branch}", null);


        stubFor(get(urlEqualTo("/repos/{username}/{repository}/branches".replace("{username}", username).replace("{repository}", repositoryTested.name())))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBodyBranches)
                )
        );

        //when
        Flux<Branch> response = githubClient.getAllBranches(repositoryTested);
        // then
        StepVerifier.create(response)
                .recordWith(ArrayList::new)
                .thenConsumeWhile(branches -> true)
                .consumeRecordedWith(branches -> {
                    assertThat(branches.size()).isEqualTo(1);
                    assertThat(branches).containsExactlyInAnyOrder(branch);
                })
                .verifyComplete();
    }

    @Test
    void should_return_not_found_error_when_user_not_found() throws IOException {
        // given
        final String badUsername = "andoidio12812h90d0aa9s0dah901";
        final String responseNotFound = IOUtils.resourceToString("/files/user-not-found-response-from-github-api.json", StandardCharsets.UTF_8);

        stubFor(get(urlEqualTo("/users/{username}/repos".replace("{username}", badUsername)))
                .willReturn(
                        aResponse()
                                .withStatus(404)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseNotFound)
                )
        );

        StepVerifier
                .create(githubClient.getAllRepositoriesByUser(badUsername))
                .expectErrorMatches(throwable ->
                        throwable instanceof ResponseStatusException &&
                                ((ResponseStatusException) throwable).getStatusCode().equals(HttpStatus.NOT_FOUND) &&
                                throwable.getMessage().contains("User not found")
                ).verify();

    }
}