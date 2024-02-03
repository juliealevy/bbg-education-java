# bbg-education-java
This is a practice java app to track the course make-up and student progress in programs offerred by the Berkshires Botanical Garden.
- Example:  Horticulture Certificate Program I  (which i am currently participating in and have to track myself on paper)

This is very much an in progress project - in current development.

Tools:  Docker, PostgreSQL for dev, H2 for testing, Spring Boot, JPA, pipelinr, lombok

## What's in it
Some sample code in Java/Spring Boot to show the following:
- Vertical Slice ish
- Architecture Boundaries/Packages:
  - api (rest api controllers)
  - application (business logic commands/services, etc)
  - domain (domain entities)
  - infrastructure (DB repositories, etc)
- First domain CRUD implementation:  BBGProgram with commands
- Integration Testing of Repositories, Commands, Controllers
- Commands/Handlers
- Repository Pattern
- Start of error handling
  - added oneOf union classes for returns to replace throwing exceptions or returning nulls
- Basic jwt bearer authentication 
  -   added UserEntity - trying out Service pattern instead of Command to compare
  -   added jwt security config, auth filter, register/login apis, etc.
- Added more entities and attached a schema diagram  
- HAL/HATEOS responses 
  - trying out Spring Boot Hateos EntityModel and EntityCollection
  - auth and program done
  - trying to figure out the best way to build the discoverable links for an api call (with templated uris)

## Coming soon
In progress, coming soon:
- Logging
- user context
- idempotent posts - caching (in memory vs distr (redus?))
- more error handling
  - better problem responses
  - authentication needs better error handling
- clean up user controller - remove unneccesary api calls handled by auth
- validation
- Next domain object:  BBGSession (Many to One with BBGProgram) with service 
- Unit Testing
- Next domain object:  Courses (many to one with bbgSession)
- TBD
  
## Discoverable API/REST calls

https://localhost:8080/api

```json
  "status": "coming soon"
```
