package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskCtrl extends Node implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Task task;
    @FXML
    private Label taskTitle;

    @Inject
    public TaskCtrl(ServerUtils server, MainCtrl mainCtrl, String title) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.taskTitle = new Label(title);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.connectToWebSockets();
        // The task list ctrl gets subscribed to the following path.
        // Now it can receive updates that are sent back from the server to this path.
        this.server.registerForMessages("/topic/tasks/edit", Task.class, task -> {
            // do something to update idk
            if (task.id == this.task.id) {
                this.mainCtrl.refreshTaskLater(task);
            }

        });
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void edit() {
        this.mainCtrl.showEditTask(this.task);
    }

    /**
     * Deletes a task from the server and removes it from the task list.
     */
    public void delete() {
        try{
            this.server.deleteTask(this.task);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //might want to come up with a better way to do delete a task from the list
        // instead of just reloading the whole board.
        this.mainCtrl.loadTaskLists();
    }

    public void openTaskOverview() {
        System.out.println("Opening task overview for task " + this.taskTitle.getText());
    }

    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }

}
