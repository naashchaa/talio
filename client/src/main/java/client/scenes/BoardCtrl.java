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
        this.mainCtrl.showAddTaskList();
    }

    public HBox getContainer() {
        return this.container;
    }
}
