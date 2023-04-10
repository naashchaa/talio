package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditTaskCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private TaskCtrl ctrl;
    @FXML
    private TextField name;

    @Inject
    public EditTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
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
        this.ctrl.getTask().setName(newName);
        this.server.send("/app/tasks/edit", this.ctrl.getTask());

        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    public TaskCtrl getTaskCtrl() {
        return this.ctrl;
    }

    public void setTaskCtrl(TaskCtrl ctrl) {
        this.ctrl = ctrl;
        this.name.setText(ctrl.getTask().getName());
    }

    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }
}
