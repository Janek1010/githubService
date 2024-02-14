package org.example.githubservice.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a branch of a repository.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO {

    /**
     * Name of the branch.
     */
    private String name;

    /**
     * Last commit associated with the branch.
     */
    private CommitDTO commit;

}
