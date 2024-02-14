# Description of API

Click the link below to access the API documentation.

[Swagger Documentation](http://localhost:8080/swagger-ui.html)

# Installation

To run the project, clone the repository and open it in an IDE (e.g. IntelliJ).
Before running the project, ensure you build it using the following command:

``mvn clean package ``

# About project

This is a simple project that allows you to query GitHub users. 
In response, you'll receive information about their repositories that are not forks. 
Each repository includes details such as Repository Name, 
Owner login, and for each branch - name and the last commit SHA.
Requests are made asynchronously using WebFlux.

Here's an example JSON response for one repository:

```
[
    {
        "name": "gimmemoji",
        "branches": [
            {
                "name": "main",
                "commit": "7123aa7c2d68f7e8ab38abbed9ea22595441b32a"
            }
        ],
        "ownerLogin": "zeto"
    }
]
```


# Testing

Basic queries are provided in the requests.http file. 
You can also use Swagger to test the API (click the link above).

# Third-party APis

The project uses the GitHub API to retrieve information about users and their repositories.
Documentation of two mainly used endpoints:
https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28#list-repositories-for-a-user
https://docs.github.com/en/rest/branches/branches?apiVersion=2022-11-28#list-branches

# Technologies

- Java 21
- Spring 6
- Spring Boot 3
- Maven
- Lombok
- Swagger
- WebFlux