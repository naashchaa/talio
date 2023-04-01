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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.lang.reflect.Type;
import java.net.URL;
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

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.simp.stomp.StompFrameHandler;
//import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    /**
     * Opens a connection with the localhost and reads the quotes directly.
     * @throws IOException if there is an exception
     */
    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

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

    public TaskList updateTaskList(TaskList taskList) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("/api/taskList/update") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(taskList, APPLICATION_JSON), TaskList.class);
    }

    //private StompSession session = this.connect("ws://localhost:8080/websocket");

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
     * Let me cook.
     * @param dest
     * @param consumer
     */
    public void registerForTaskLists(String dest, Consumer<TaskList> consumer) {
//        this.session.subscribe(dest, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return TaskList.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                consumer.accept((TaskList) payload);
//            }
//        });
    }

    /**
     * Sends an object to a destination.
     * @param dest destination
     * @param o object
     */
    public void send(String dest, Object o) {
//        this.session.send(dest, o);
    }

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