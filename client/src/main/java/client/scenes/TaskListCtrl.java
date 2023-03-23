package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class TaskListCtrl extends Node{
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String name;
    @FXML
    private VBox taskContainer;

    @Inject
    public TaskListCtrl(ServerUtils server, MainCtrl mainCtrl,String name) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.name = name;
    }

    public void addTask() {
        this.mainCtrl.showAddTask();
        Pair<TaskCtrl, Parent> pair =
                Main.FXML.load(TaskCtrl.class, "client", "scenes", "Task.fxml");
        this.taskContainer.getChildren().add(pair.getValue());
    }
}
