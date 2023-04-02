package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class TaskCtrl extends Node {
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

    public void setTask(Task task) {
        this.task = task;
    }

    public void edit() {
        this.mainCtrl.showEditTask(this.task);
    }

    public void delete() { System.out.println("Deleting task...");}

    public void openTaskOverview() {
        System.out.println("Opening task overview for task " + this.taskTitle.getText());
    }

}
