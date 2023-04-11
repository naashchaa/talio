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

import java.io.*;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
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
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private static String SERVER = "http://localhost:8080/";

    public String getConnectionString() {
        return SERVER;
    }

    /** This method modifies the SERVER connection string.
     * @param string the string to set SERVER to
     * @return true if the connection was successful, false otherwise
     */
    public boolean setConnectionString(String string) {
        String temp = SERVER;
        try {
            SERVER = string;
            List<Board> boards = this.getBoards();
            return boards != null;
        }
        catch (RuntimeException e){
            SERVER = temp;
            return false;
        }
    }

    /** This method returns only the boards that the client has joined.
     * @return the list of joined boards
     */
    public List<Board> getRememberedBoards() {
        List<Long> ids = this.parseRememberedBoardIds();
        List<Board> boards = new LinkedList<>();

        for (long id : ids) {
            boards.add(this.getBoard(id));
        }

        return boards;
    }

    /**
     * This method makes sure that there is a contexts.txt config file present.
     */
    public void ensureContextConfigPresence() {
        try {
            Scanner contextsScanner = new Scanner(new File("config/contexts.txt"));
            if (!contextsScanner.hasNext() ||
                contextsScanner.next().equals(System.lineSeparator()))
                throw new FileNotFoundException();
        }
        catch (FileNotFoundException e) {
            try {
                PrintWriter writer = new PrintWriter(
                    new FileWriter("config/contexts.txt"));

                writer.println(SERVER);
                writer.println();
                writer.close();
            }
            catch (IOException eee) {
                throw new RuntimeException(eee);
            }
        }
    }

    /**
     * This method will write a new entry for a server it wasn't present in the contexts.txt yet.
     */
    public void writeNewServerContext() {
        try {
            PrintWriter writer = new PrintWriter(
                new FileWriter("config/contexts.txt", true));

            writer.println(SERVER);
            writer.println();
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** This method parses all boards that a client visited in a given server.
     * @return List of ids of visited boards
     */
    public List<Long> parseRememberedBoardIds() {
        List<Long> ids = new LinkedList<>();

        // check if the config is present
        this.ensureContextConfigPresence();

        try {
            Scanner contextsScanner = new Scanner(new File("config/contexts.txt"));
            contextsScanner.useDelimiter(System.lineSeparator() + System.lineSeparator());

            Optional<String> context = contextsScanner.tokens()
                .filter(lines -> lines.startsWith(SERVER))
                .findFirst();

            if(context.isEmpty())
                this.writeNewServerContext();

            contextsScanner = new Scanner(new File("config/contexts.txt"));
            contextsScanner.useDelimiter(System.lineSeparator() + System.lineSeparator());

            context = contextsScanner.tokens()
                .filter(lines -> lines.startsWith(SERVER))
                .findFirst();

            if (context.isEmpty())
                throw new RuntimeException("I don't know what to do if this fails.");

            Scanner contextScanner = new Scanner(context.get());
            contextScanner.useDelimiter(System.lineSeparator());

            if (contextScanner.hasNext())   //redundant if, but java doesn't know
                contextScanner.next();

            while (contextScanner.hasNext()) {
                ids.add(Long.parseLong(contextScanner.next()));
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Could not load the contexts config file");
        }

        return ids;
    }

    public List<Board> getBoards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
    }

    public Board getBoard(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(SERVER).path("api/boards/" + id) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<>() {});
    }

    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public void deleteTaskListWrapper(TaskList taskList) {
        // delete all children
        this.deleteTasksByParentList(taskList);

        // delete the task list itself
        this.deleteTaskList(taskList);
    }

    public void deleteTaskList(TaskList taskList) {
        ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/tasklists/" + taskList.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .delete();
    }

    /**
     * This method returns all tasks for a certain task list.
     * @param tasklist the task list of which to fetch the tasks
     * @return returns a list of tasks.
     */
    public List<Task> getTasksByParentList(TaskList tasklist) {
        String id = Long.toString(tasklist.getId());
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/tasks/get_by_parent/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Gets the id of a task list and by that removes all of its internal tasks.
     * @param taskList the task list whose tasks need to be removed.
     */
    public void deleteTasksByParentList(TaskList taskList) {
        ClientBuilder.newClient(new ClientConfig())
                            .target(SERVER).path("api/tasks/delete_by_parent/" + taskList.getId())
                            .request(APPLICATION_JSON)
                            .accept(APPLICATION_JSON)
                                .delete();
    }

    public List<TaskList> getTaskListsByParentBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
            .target(SERVER).path("api/tasklists/get_by_parent/" + board.getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<>() {});
    }

    public void deleteBoard(Board board) {
        ClientBuilder.newClient(new ClientConfig())
        .target(SERVER).path("api/boards/" + board.getId())
        .request(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
            .delete();
    }

    /**
     * @param board the board from which everything should be deleted
     */
    public void deleteEverythingOfBoard(Board board) {
        List<TaskList> taskLists = this.getTaskListsByParentBoard(board);
        for (TaskList taskList : taskLists) {
            this.deleteTaskListWrapper(taskList);
        }
        this.deleteBoard(board);
    }

    public void editBoard(Board board) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("/api/boards/update") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * @param id the id of the board to check
     * @return true if the board exists, false otherwise
     */
    public boolean checkBoardExists(Long id) {
        try{
            ClientBuilder.newClient(new ClientConfig())
                    .target(SERVER).path("/api/boards/" + id)
                    .request(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .get(new GenericType<>() {
                    });
            return true;
        }
        catch (RuntimeException e){
            return false;
        }
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
    @SuppressWarnings("unchecked")
    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {

        /*
          A ctrl that has registered to receive messages will get subscribed
          to the destination that has been passed as an argument. This is a
          function of STOMP.
         */
        this.session.subscribe(dest, new StompFrameHandler() {

            @Override
            public @NotNull Type getPayloadType(@NonNull StompHeaders headers) {
                return type;
            }

            @Override
            public void handleFrame(@NonNull StompHeaders headers, Object payload) {

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

    // LONG POLLING

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    /**
     * Detects if there has been any changes in the board to a task list.
     * @param consumer a consumer
     */
    public void registerForTaskListsL(Consumer<TaskList> consumer) {
        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient(new ClientConfig()) //
                        .target(SERVER).path("api/tasklists/updates") //
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