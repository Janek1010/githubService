package org.example.githubservice.model;

import java.util.List;

public record Repository(String name, Owner owner, Boolean fork, String branchesUrl, List<Branch> branches) {
}
