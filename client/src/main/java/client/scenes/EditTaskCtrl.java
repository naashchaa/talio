package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditTaskCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Task task;
    @FXML
    private TextField name;
    private boolean isConnected;

    @Inject
    public EditTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isConnected = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        this.connectToWebSockets();
    }

    /**
     * Closes the edit task popup.
     */
    public void cancel() {
        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    /**
     * Takes the user's input passes it to the database and
     * then loads the new name in the task list controller.
     */
    public void confirm() {
        String newName = this.name.getText();
        this.task.setName(newName);
        this.server.send("/app/tasks/edit", this.task);

        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    public void setTask(Task task) {
        this.task = task;
        this.name.setText(task.getName());
    }

    public void connectToWebSockets() {
        if (this.isConnected)
            return;
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
        this.isConnected = true;
    }
}
