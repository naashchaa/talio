package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditTaskListCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private TaskList taskList;
    @FXML
    private TextField name;

    @Inject
    public EditTaskListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void cancel() {
        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    public void confirm() {
        this.server.updateTaskList(this.taskList);
        Label label = (Label) taskList.lookup("#taskListName");
        label.setText(taskList.getName());
        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
        name.setText(taskList.getName());

    }
}
