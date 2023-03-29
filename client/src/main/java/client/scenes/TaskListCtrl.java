package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class TaskListCtrl extends Node {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private TaskList taskList;
    @FXML
    private VBox taskContainer;

    @Inject
    public TaskListCtrl(ServerUtils server, MainCtrl mainCtrl, String name) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void addTask() {
        this.mainCtrl.showAddTask(this);
    }

    public void addTaskToList(String name) {
        Pair<TaskCtrl, Parent> pair =
                Main.FXML.load(TaskCtrl.class, "client", "scenes", "Task.fxml");
        Label label = (Label) pair.getValue().lookup("#taskTitle");
        label.setText(name);
        this.taskContainer.getChildren().add(pair.getValue());
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public TaskList getTaskList() {
        return this.taskList;
    }

    public void edit() {
        this.mainCtrl.showEditTaskList(this.taskList);
    }
}
