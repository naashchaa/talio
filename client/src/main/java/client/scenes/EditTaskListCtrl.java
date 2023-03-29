package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;

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

    /**
     * Cancels the modification of the task list
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
        this.taskList.setName(newName);
        this.server.updateTaskList(this.taskList);

        List<TaskListCtrl> taskLists = this.mainCtrl.getTaskListCtrls();
        for (TaskListCtrl listCtrl : taskLists) {
            if (this.taskList.equals(listCtrl.getTaskList())) {
                listCtrl.setTaskListName(newName);
            }
        }
        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
        this.name.setText(taskList.getName());

    }
}
