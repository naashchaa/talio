package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditTaskCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Task task;
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
        this.task.setName(newName);
        this.task = this.server.updateTask(this.task);

        // may need a run later with web sockets
        this.mainCtrl.loadTasks(this.task.getParentTaskList());

        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    public void setTask(Task task) {
        this.task = task;
        this.name.setText(task.getName());
    }
}
