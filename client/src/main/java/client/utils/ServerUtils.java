/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import commons.Board;
import commons.Task;
import commons.TaskList;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private static String SERVER = "http://localhost:8080/";

    public Task addTask(Task task) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/tasks")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    public List<Board> getBoard() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {});
    }

    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public TaskList addTaskList(TaskList tasklist) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/taskList")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(tasklist, APPLICATION_JSON), TaskList.class);
    }

    public void deleteTaskList(TaskList taskList) {
        ClientBuilder.newClient(new ClientConfig())
        .target(SERVER).path("api/taskList/" + taskList.getId())
        .request(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
            .delete();
    }

    public List<TaskList> getTaskLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/taskList") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<TaskList>>() {});
    }

    /**
     * I'm not yet sure this works.
     * @param tasklist
     * @return returns a list of tasks.
     */
    public List<Task> getTasks(TaskList tasklist) {
        String id = Long.toString(tasklist.getId());
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/tasks/get_by_parent/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Task>>() {});
    }

    /**
     * Gets the id of a task list and by that removes all of its internal tasks.
     * @param taskList the task list whose tasks need to be removed.
     */
    public void deleteTasksParentTaskList(TaskList taskList) {
        var a = ClientBuilder.newClient(new ClientConfig())
                            .target(SERVER).path("api/tasks/delete_by_parent/" + taskList.getId())
                            .request(APPLICATION_JSON)
                            .accept(APPLICATION_JSON)
                                .delete();
        System.out.println();
    }

    public void deleteTask(Task task) {
        ClientBuilder.newClient(new ClientConfig())
        .target(SERVER).path("api/tasks/" + task.getId())
        .request(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
            .delete();
    }

    /** This method modifies the SERVER connection string.
     * @param string the string to set SERVER to
     * @return true if the connection was successful, false otherwise
     */
    public boolean setConnectionString(String string) {
        String temp = SERVER;
        try {
            SERVER = string;
            List<Board> boards = this.getBoard();
            return true;
        }
        catch (RuntimeException e){
            SERVER = temp;
            return false;
        }
    }

    /** Retrieves the provided TaskList from the repository
     * (used for TaskList objects without assigned IDs).
     * @param taskList TaskList to be returned
     * @return the TaskList entry from the database
     */
    public TaskList getTaskList(TaskList taskList) {
        String id = Long.toString(taskList.getId());

        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("/api/taskList/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<TaskList>() {});
    }

    public Task updateTask(Task task) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(SERVER).path("/api/tasks/" + task.id + "/update") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(task, APPLICATION_JSON), Task.class);
    }

    public TaskList updateTaskList(TaskList taskList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("/api/taskList/update") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(taskList, APPLICATION_JSON), TaskList.class);
    }

    // WEB SOCKETS
    private StompSession session;

    /**
     * This method establishes the websocket connection.
     */
    public void establishWebSocketConnection() {
        String connectionString = "ws" + SERVER.substring(4);
        if (!connectionString.endsWith("/"))
            connectionString += "/";

        connectionString += "websocket";
        this.session = this.connect(connectionString);
    }

    public void terminateWebSocketConnection() {
        if (this.session != null)
            this.session.disconnect();
    }

    /**
     * Default connect method from documentation.
     * @param url the url
     * @return a stomp session
     */
    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException e) {
            throw new RuntimeException();
        }
        throw new IllegalStateException();
    }

    /**
     * Task list ctrls subscribe to receiving messages from websockets.
     * @param dest what destination in web sockets to send the message.
     * @param type the type of class sent to the method.
     * @param consumer the consumer.
     * @param <T> type.
     */
    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {

        /**
         * A ctrl that has registered to receive messages will get subscribed
         * to the destination that has been passed as an argument. This is a
         * function of STOMP.
         */
        this.session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * A ctrl can call send if it needs all the subscribers to
     * receive the update.
     * @param dest destination
     * @param o object
     */
    public void send(String dest, Object o) {
        this.session.send(dest, o);
    }

    public void disconnectStompSession() {
        this.session.disconnect();
    }

    // LONG POLLING

    private static ExecutorService EXEC = Executors.newSingleThreadExecutor();

    /**
     * Detects if there has been any changes in the board to a task list.
     * @param consumer a consumer
     */
    public void registerForTaskListsL(Consumer<TaskList> consumer) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig()) //
                        .target(SERVER).path("api/taskList/updates") //
                        .request(APPLICATION_JSON) //
                        .accept(APPLICATION_JSON) //
                        .get(Response.class);
                if (res.getStatus() == 204) {
                    continue;
                }
                var taskList = res.readEntity(TaskList.class);
                consumer.accept(taskList);
            }
        });
    }

    public void stop() {
        EXEC.shutdownNow();
    }

}