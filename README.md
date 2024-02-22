# bbg-education-java
This is a practice java app to track the course make-up and student progress in programs offerred by the Berkshires Botanical Garden.
- Example:  Horticulture Certificate Program I  (which i am currently participating in and have to track myself on paper)

This is very much an in progress project - in current development.

Tools:  Docker, PostgreSQL for dev, H2 for testing, Spring Boot, JPA, pipelinr, lombok, fluent-validator

Documentation is in documents folder:  https://github.com/juliealevy/bbg-education-java/tree/main/src/main/documents

## What's in it
Some sample code in Java/Spring Boot to show the following:
- Vertical Slice ish
- Architecture Boundaries/Packages:
  - api (rest api controllers)
  - application (business logic commands/services, etc)
  - domain (domain entities)
  - infrastructure (DB repositories, auth, etc)
- First domain CRUD implementation:  BBGProgram with commands
- Integration Testing of controllers and command pipelines
- Unit Testing of commands, services, repositories
- Commands/Handlers
- Repository Pattern
- Start of error handling
  - added oneOf union classes for returns to replace throwing exceptions or returning nulls
  - catch-all global handler for thrown exceptions: ExceptionHandlerController (@RestControllerAdvice)
- Basic jwt bearer authentication 
  -   added UserEntity - trying out Service pattern instead of Command to compare
  -   added jwt security config, auth filter, register/login apis, etc.
  -   added admin role with method-level security
  -   added refresh token
- Added more entities and attached a schema diagram  
- HAL/HATEOS responses 
  - trying out Spring Boot Hateos EntityModel and EntityCollection
    - auth and program done
  - building discoverable links with templated hrefs using application context request method handlers
    - auth and program done (response json below)
- Added command-based validation in a pipeline middleware.
  -   implemented for CreateProgram
  -   implemented for ProgramUpdate  
- Added logging
  - Initial logging config, a few examples.  (in progress) 
  - Added request/response logging with sensitive data scrubbing
- Added basic CRUD for Session (child of Program)  
  -   decided to go with Command Pattern
- Added actuator for health check, metrics, etc.
- Started CRUD for Course - in progress
    

## Coming soon
- add some more workflow oriented calls for Session once Courses implemented
- validation for services based entities (User/Auth)
- Auth: consider implementing revoke feature (with DB persistance of tokens)
- Caching of some data (maybe use Redis in Docker)
- idempotent posts - caching (in memory vs distr (redus?))
- more error handling
  - better problem response building/handling
- Focus on more workflow oriented implementation for rest of entities/features/workflows  
  
## Discoverable API/REST calls

https://localhost:8080/api

```json
{
    "version": "1.0.0",
    "_links": {
        "self": {
            "href": "/api"
        },
        "auth:register": {
            "href": "/api/auth/register",
            "httpMethod": "POST",
            "body": {
                "email": "string",
                "password": "string",
                "firstName": "string",
                "lastName": "string",
                "isAdmin": "boolean"
            }
        },
        "auth:login": {
            "href": "/api/auth/login",
            "httpMethod": "POST",
            "body": {
                "email": "string",
                "password": "string"
            }
        },
        "auth:refresh": {
            "href": "/api/auth/refresh",
            "httpMethod": "POST"
        },
        "program:create": {
            "href": "/api/programs",
            "httpMethod": "POST",
            "body": {
                "name": "string",
                "description": "string"
            }
        },
        "program:update": {
            "href": "/api/programs/{pid}",
            "httpMethod": "PUT",
            "body": {
                "name": "string",
                "description": "string"
            },
            "templated": true
        },
        "program:delete": {
            "href": "/api/programs/{pid}",
            "httpMethod": "DELETE",
            "templated": true
        },
        "program:get-by-id": {
            "href": "/api/programs/{pid}",
            "httpMethod": "GET",
            "templated": true
        },
        "program:get-all": {
            "href": "/api/programs",
            "httpMethod": "GET"
        },
        "session:create": {
            "href": "/api/programs/{pid}/sessions",
            "httpMethod": "POST",
            "body": {
                "name": "string",
                "description": "string",
                "startDate": "MM-dd-yyyy",
                "endDate": "MM-dd-yyyy",
                "practicumHours": "Integer"
            },
            "templated": true
        },
        "session:get-by-id": {
            "href": "/api/programs/{pid}/sessions/{sid}",
            "httpMethod": "GET",
            "templated": true
        },
        "session:get-by-program": {
            "href": "/api/programs/{pid}/sessions",
            "httpMethod": "GET",
            "templated": true
        },
        "session:delete": {
            "href": "/api/programs/{pid}/sessions/{sid}",
            "httpMethod": "DELETE",
            "templated": true
        },
        "session:update": {
            "href": "/api/programs/{pid}/sessions/{sid}",
            "httpMethod": "PUT",
            "body": {
                "name": "string",
                "description": "string",
                "startDate": "MM-dd-yyyy",
                "endDate": "MM-dd-yyyy",
                "practicumHours": "Integer"
            },
            "templated": true
        },
        "course:create": {
            "href": "/api/courses",
            "httpMethod": "POST",
            "body": {
                "name": "string",
                "description": "string",
                "isPublic": "boolean",
                "isOnline": "boolean"
            }
        },
        "course:delete": {
            "href": "/api/courses/{cid}",
            "httpMethod": "DELETE",
            "templated": true
        },
        "course:get-by-id": {
            "href": "/api/courses/{cid}",
            "httpMethod": "GET",
            "templated": true
        },
        "course:get-all": {
            "href": "/api/courses",
            "httpMethod": "GET"
        }
    }
}
```
