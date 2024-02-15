package org.example.githubservice.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.githubservice.client.api.GithubClient;
import org.example.githubservice.model.dtos.RepositoryDTO;
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

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@SpringBootTest
@WireMockTest(httpPort = 8081)
@ExtendWith(SpringExtension.class)
public class GithubControllerIT {
    @Autowired
    GithubClient githubClient;
    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("github.api.base-url", () -> "http://localhost:8081");
    }

    @Test
    void should_return_correct_repositories() throws IOException {
        // given
        String responseBodyRepositories = IOUtils.resourceToString("/files/correct-response-repositories-from-githubAPI.json", StandardCharsets.UTF_8);
        String responseBodyBranches = IOUtils.resourceToString("/files/correct-response-branches-from-githubAPI.json", StandardCharsets.UTF_8);
        String username = "jotzet";
        String repositoryName = "gimmemoji";

        stubFor(get(urlEqualTo("/users/{username}/repos".replace("{username}", username)))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBodyRepositories)
                )
        );

        stubFor(get(urlEqualTo("/repos/{username}/{repository}/branches".replace("{username}", username).replace("{repository}", repositoryName)))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBodyBranches)
                )
        );
        //when
        Flux<RepositoryDTO> response = githubClient.getAllRepositoriesByUser("jotzet");

        // then
        response.doOnNext(repositoryDTO -> {
            System.out.println(repositoryDTO.name());
            System.out.println(repositoryDTO.owner().login());
            System.out.println(repositoryDTO.branches());
        }).blockLast();
    }

}


//    @Autowired
//    WebTestClient webTestClient;
//
//    @Test
//    void testListAllRepositoriesOfUser() {
//        // Given
//        String username = "Kondziow";
//        int page = 1;
//        int perPage = 2;
//
//        // When/Then
//        webTestClient.get()
//                .uri("/api/v1/users/{username}/repos?page={page}&perPage={perPage}", username, page, perPage)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody()
//                .jsonPath("$[0].name").exists()
//                .jsonPath("$[1].name").exists();
//    }
//
//    @Test
//    void testUserNotFound() {
//        // Given
//        String username = "12i9b12dsblasdklaasd";
//
//        // When/Then
//        webTestClient.get()
//                .uri("/api/v1/users/{username}/repos", username)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody()
//                .jsonPath("$.status").exists()
//                .jsonPath("$.message").exists();
//    }

