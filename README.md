# toggler
 Real-time, dynamic and hierarchical management of toggles for services and applications.

 Toggler is a service developed in Java 11 with Spring Boot and Postgresql database. The service is provided through a REST API with the following methods:

 http://host:8080/toggles

 Operations related to creation, activation, deactivation or change of state of toggles (for all clients or only to a specific client)
 
 http://host:8080/toggles-overrides

 Operations related to creation, activation or deactivation of toggles overrides for specific clients
 
 http://host:8080/toggles-exclusions
 Operations related to creation, activation or deactivation of toggles exclusions for specific clients
 
 Explore the online documentation for theses methods at http://host:8080/swagger-ui.html
