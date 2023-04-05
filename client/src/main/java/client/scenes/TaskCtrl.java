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
        // Subscribing the task controller to the edit path
        this.server.registerForMessages("/topic/tasks/edit", Task.class, task -> {
            if (task.id == this.task.id) {
                this.mainCtrl.refreshTaskLater(task);
            }
        });
        // Subscribing the task controller to the delete path
        this.server.registerForMessages("/topic/tasks/delete", Task.class, task -> {
            if (task.id == this.task.id) {
                this.mainCtrl.deleteTaskLater(task);
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
        this.server.send("/app/tasks/delete", this.task);
    }

    public void openTaskOverview() {
        System.out.println("Opening task overview for task " + this.taskTitle.getText());
    }

    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }

}
