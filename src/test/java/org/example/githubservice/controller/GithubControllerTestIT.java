package org.example.githubservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.example.githubservice.model.Owner;
import org.example.githubservice.model.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import wiremock.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WireMockTest // it will take random port, it includes also @ExtendWith(WireMockExtension.class)
@AutoConfigureWebTestClient
class GithubControllerTestIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void shouldReturnCorrectResponseWithRepositoriesAndBranches() throws IOException {
        // given
        final String responseBodyRepositories = IOUtils.resourceToString("/files/correct-response-repositories-from-githubAPI.json", StandardCharsets.UTF_8);
        final String responseBodyBranches = IOUtils.resourceToString("/files/correct-response-branches-from-githubAPI.json", StandardCharsets.UTF_8);
        String expectedResponseBody = IOUtils.resourceToString("/files/correct-response-full-from-application.json", StandardCharsets.UTF_8);
        final String username = "jotzet";
        Repository repositoryTested = new Repository("gimmemoji", new Owner(username), false, "https://api.github.com/repos/jotzet/gimmemoji/branches{/branch}", null);

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

        ObjectMapper mapper = new ObjectMapper();

        webTestClient.get()
                .uri("/api/v1/users/{username}/repos", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .value(responseBody -> {
                    try {
                        JsonNode expectedNode = mapper.readTree(expectedResponseBody);
                        JsonNode actualNode = mapper.readTree(responseBody);

                        assertEquals(expectedNode, actualNode);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    void shouldReturnNotFoundResponse() throws IOException {
        // given
        final String userNotFoundResponseGithHub = IOUtils.resourceToString("/files/user-not-found-response-from-github-api.json", StandardCharsets.UTF_8);
        final String expectedUserNotFoundBody = IOUtils.resourceToString("/files/user-not-found-from-application.json", StandardCharsets.UTF_8);
        final String badUsername = "20981x0n321x9012x029190x2";

        stubFor(get(urlEqualTo("/users/{username}/repos".replace("{username}", badUsername)))
                .willReturn(
                        aResponse()
                                .withStatus(404)
                                .withHeader("Content-Type", "application/json")
                                .withBody(userNotFoundResponseGithHub)
                )
        );

        ObjectMapper mapper = new ObjectMapper();

        webTestClient.get()
                .uri("/api/v1/users/{username}/repos", badUsername)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .value(responseBody -> {
                    try {
                        JsonNode expectedNode = mapper.readTree(expectedUserNotFoundBody);
                        JsonNode actualNode = mapper.readTree(responseBody);

                        assertEquals(expectedNode, actualNode);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}