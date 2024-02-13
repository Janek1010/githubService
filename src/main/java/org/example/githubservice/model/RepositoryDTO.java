package org.example.githubservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryDTO {
    private String fork;
    private String name;
    private OwnerDTO owner;

}
