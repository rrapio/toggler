# toggler
 Real-time, dynamic and hierarchical management of toggles for services and applications.

 **Toggler** is a service developed in Java 11, Spring Boot and Postgresql database. The main purpose of the service is to provide real-time, dynamic and hierarchical on/off switches. Clients can change or check the status of toggles, besides activating or deactivating them. Since the status of toggles can be easily checked through REST API endpoints (HATEOAS-compliant), functionalities can be wired to be dependant on the state of certain toggles, allowing a controlled, safe and gradual selection of features to be enabled in production. The service also allows the creation of a list of excluded clients who cannot see certains toggles and toggles whose state is overriden for certain clients. These features are available through the control of the following entities: 
 
 ***toggles*** are on/off switches for all clients (services and applications). The main property is *toggleIdentifier*, the textual identifier for the toggle and that, ultimately, will be used to compose the URL for the toggle. The boolean property *value* controls the current state of the toggle (on/off). ***toggles*** can also be activated or deactivated, what is reflected by the boolean *active* property.
 
 ***toggles-exclusions*** reflect for a specific toggle which clients are excluded from the access list, or in other words, which clients will not be able to see a specific toggle. This entity contains the *toggleIdentifier* property where there is the key for the toggle, whereas *serviceIdentifier* and *serviceVersion* are used to identify the service or application excluded. ***toggles-exclusions*** can also be active or inactive through the property *active*.
 
 ***toggles-overrides*** are responsible for managing toggles who (no matter if they exist or not as a toggle for all clients) will exist as a separate, exclusive state for a specific client. ***toggles-overrides*** have the key properties *toggleIdentifier*, *serviceIdentifier* and *serviceVersion*. Just like with a toggle, ***toggles-overrides*** have properties *value* and *active*, who will store the information used to control respectively state and availability.
 
 ---
 
 The service is provided through a REST API with the following URL paths:

 http://host:8080/toggles

 Operations related to creation, activation, deactivation or change of state of toggles (for all clients or only to a specific client)
 
 http://host:8080/toggles-overrides

 Operations related to creation, activation or deactivation of toggles overrides for specific clients
 
 http://host:8080/toggles-exclusions
 
 Operations related to creation, activation or deactivation of toggles exclusions for specific clients
 
 Explore the online documentation for more details on how to use these operations at http://host:8080/swagger-ui.html (available after build and deploy).
 
 ---
 
 The project comes with the files required for a Maven build (https://maven.apache.org/).
