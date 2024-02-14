# Description of API

Click in the link below to see the documentation of the API.

[dokumentacja swagger](http://localhost:8080/swagger-ui.html)

# Installation

To run the project, clone the repository and run in IDE (e.g. intellij), 
you should also firstly build the project with command:

``mvn clean package ``

# About project

This is a simple project that allows you to query for user and in 
response you will get information about his repostiories which are't
forks. Every repository include informaiton about Repository Name,
Owner login and for each branch - name and last commit sha.
Example of response in JSON:
```
[
  {
    "name": "Hyperskill",
    "owner": {
      "login": "Mondziow"
    },
    "branches": [
      {
        "name": "main",
        "commit": "09d3ce93bbf71e6c0c3d279a6f5c516f61e44cbb"
      }
    ],
    "branches_url": "https://api.github.com/repos/Mondziow/Hyperskill/branches{/branch}"
  },
  {
    "name": "Java-Spring-Simple-Speedway-App",
    "owner": {
      "login": "Mondziow"
    },
    "branches": [
      {
        "name": "main",
        "commit": "32119486964416c37ee69909d4b6d1c79d2e637f"
      }
    ],
    "branches_url": "https://api.github.com/repos/Mondziow/Java-Spring-Simple-Speedway-App/branches{/branch}"
  }
]
```


# Testing

I've prepared some basic queries in requests.http file.
You can also use swagger to test the API. (click on the link above)