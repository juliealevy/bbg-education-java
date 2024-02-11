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
- Integration Testing of Repositories, Commands, Controllers
- Unit Testing of commands and services
- Commands/Handlers
- Repository Pattern
- Start of error handling
  - added oneOf union classes for returns to replace throwing exceptions or returning nulls
- Basic jwt bearer authentication 
  -   added UserEntity - trying out Service pattern instead of Command to compare
  -   added jwt security config, auth filter, register/login apis, etc.
  -   added admin role with method-level security
  -   added refresh token
- Added more entities and attached a schema diagram  
- HAL/HATEOS responses 
  - trying out Spring Boot Hateos EntityModel and EntityCollection
    - auth and program done
- Added command-based validation in a pipeline middleware.
  -   implemented for CreateProgram
  -   implemented for ProgramUpdate  
- Added logging
  - Initial logging config, a few examples.  (in progress) 
  - Added request/response logging with sensitive data scrubbing 
    

## Coming soon
- validation for services based entities (User/Auth)
- make a decision about future workflow/entity implementation:  command vs service
- trying to figure out the best way to build the discoverable links for an api call (with templated uris)
- Auth: consider implementing revoke feature (with DB persistance of tokens)
- Caching of some data (maybe use Redis in Docker)
- idempotent posts - caching (in memory vs distr (redus?))
- more error handling
  - better problem response building/handling
- Next domain object:  BBGSession (Many to One with BBGProgram)
- Focus on more workflow oriented implementation for rest of entities/features/workflows  
  
## Discoverable API/REST calls

https://localhost:8080/api

```json
  "status": "coming soon"
```
