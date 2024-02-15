package org.example.githubservice.model.dtos;

import java.util.List;


public record RepositoryDTO(
        String name,
        String ownerLogin,
        List<BranchDTO> branches
) {
}
