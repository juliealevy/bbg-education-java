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
  - adding oneOf union classes for returns to replace throwing exceptions or returning nulls (in progress)



## Coming soon
In progress, coming soon:
- HAL/HATEOS responses
- Basic jwt bearer authentication- 
- Logging
- more error handling - better problem responses
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
