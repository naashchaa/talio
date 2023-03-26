package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TaskCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Label taskTitle;

    @Inject
    public TaskCtrl(ServerUtils server, MainCtrl mainCtrl, String title) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.taskTitle = new Label(title);
    }

    public void deleteTask() { System.out.println("Deleting task...");}

    public void openTaskOverview() {
        System.out.println("Opening task overview for task " + this.taskTitle.getText());
    }

}
