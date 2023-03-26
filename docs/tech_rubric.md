# Rubric: Technology

The students are not supposed to submit a specific assignment, instead, you are supposed to look into their code base.

### Dependency Injection

Application uses dependency injection to connect dependent components. No use of static fields in classes.
- *Sufficient:* There is one example in the client code and one in the server code that uses dependency injection to connect dependent components. Static fields and methods are only sparely used to access other components.

There is exists a bean in the server side, and all scenes except for mainCtrl uses injection on the client side, static methods are the main methods and nothing else


### Spring Boot

Application makes good use of the presented Spring built-in concepts to configure the server and maintain the lifecycle of the various server components.
- *Good:* The application contains example of @Controller, @RestController, and a JPA repository.

we have multiple jpa repositories (board, task and taskList), as well as multiple @RestController (board, task and taskList) and we have some @Controller, which are currently not being used (SomeController, TestGetControllerArray, TestPostController)


### JavaFX

Application uses JavaFX for the client and makes good use of available features (use of buttons/images/lists/formatting/…). The connected JavaFX controllers are used with dependency injection.
- *Excellent:* The JavaFX controllers are used with dependency injection. 

All controllers except for main have dependency injection, we use scroll bars to scroll through tasks and taskLists 


### Communication

Application uses communication via REST requests and Websockets. The code is leveraging the canonical Spring techniques for endpoints and websocket that have been introduced in the lectures. The client uses libraries to simplify access.
- *Excellent:* The server defines all REST and webservice endpoints through Spring and uses a client library like Jersey (REST) or Stomp (Webservice) to simplify the server requests.

We use RestController (BoardController, TaskController, TaskListController) and use jersey for the client config in the ServerUtils.java

### Data Transfer

Application defines meaningful data structures and uses Jackson to perform the de-/serialization of submitted data.
- *Excellent:* Jackson is used implicitly by Spring or the client library. No explicit Jackson calls are required in the application.

ServerUtils.java uses jackson for every post request, and we have multiple @RestController which use jackson