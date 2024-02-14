package org.example.githubservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
@SpringBootTest
@AutoConfigureWebTestClient
public class GithubControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testListAllRepositoriesOfUser() {
        // Given
        String username = "Kondziow";
        int page = 1;
        int perPage = 2;

        // When/Then
        webTestClient.get()
                .uri("/api/v1/users/{username}/repos?page={page}&perPage={perPage}", username, page, perPage)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].name").exists()
                .jsonPath("$[1].name").exists();
    }

    @Test
    void testUserNotFound() {
        // Given
        String username = "12i9b12dsblasdklaasd";

        // When/Then
        webTestClient.get()
                .uri("/api/v1/users/{username}/repos", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").exists()
                .jsonPath("$.message").exists();
    }
}
