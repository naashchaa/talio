package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditTaskListCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private TaskListCtrl ctrl;
    @FXML
    private TextField name;

    @Inject
    public EditTaskListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Cancels the modification of the task list.
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
        this.ctrl.getTaskList().setName(newName);
        this.server.send("/app/tasklists/edit", this.ctrl.getTaskList());
        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    public void setTaskListCtrl(TaskListCtrl ctrl) {
        this.ctrl = ctrl;
        this.name.setText(ctrl.getTaskList().getName());
    }

    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }
}
