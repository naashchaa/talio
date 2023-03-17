package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

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

    public void addTaskList() {
        mainCtrl.showAddTaskList();
//
//        //object TaskList takes the parameters introduced by the user and creates a new instance with them
//        TaskListCtrl taskList = new TaskListCtrl(server, mainCtrl, "hello");
//        //the TaskList is added to the list of children of the HBox
//        container.getChildren().add(taskList);
    }
}
