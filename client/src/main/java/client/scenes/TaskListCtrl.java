package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskListCtrl extends Node{
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String name;

    @Inject
    public TaskListCtrl(ServerUtils server, MainCtrl mainCtrl,String name) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.name = name;
    }

    public static void initialize(URL location, ResourceBundle resources) throws IOException {
        Node newTaskList = FXMLLoader.load(getClass().getClassLoader().getResource("client/scenes/TaskList.fxml"));
        BoardCtrl b = new BoardCtrl(this.server, this.mainCtrl);
        b.getContainer().getChildren().add(newTaskList);
    }

    public void addTask() {
        this.mainCtrl.showAddTask();
    }
}
