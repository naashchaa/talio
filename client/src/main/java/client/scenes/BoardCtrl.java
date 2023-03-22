package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.awt.*;


public class BoardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox container;

    @Inject
    public BoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void addTaskListToBoard() {
        try{
            Pair<TaskListCtrl, Parent> pair = Main.FXML.load(TaskListCtrl.class, "client", "scenes", "TaskList.fxml");
            Node button = container.getChildren().get(container.getChildren().size()-1);
            container.getChildren().set(container.getChildren().size()-1, pair.getValue());
            container.getChildren().add(button);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addTask() {
        this.mainCtrl.showAddTask();
    }

    public void addTaskList() {
        this.mainCtrl.showAddTaskList();

    }
}
