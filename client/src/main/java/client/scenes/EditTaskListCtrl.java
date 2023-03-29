package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;

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

    public void cancel() {
        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

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
        name.setText(taskList.getName());

    }
}
