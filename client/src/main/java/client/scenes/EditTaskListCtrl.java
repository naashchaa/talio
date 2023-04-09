package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditTaskListCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private TaskList taskList;
    private boolean isConnected;
    @FXML
    private TextField name;

    @Inject
    public EditTaskListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isConnected = false;
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
        this.taskList.setName(newName);
//        this.server.updateTaskList(this.taskList);

//        List<TaskListCtrl> taskLists = this.mainCtrl.getTaskListCtrls();
//        for (TaskListCtrl listCtrl : taskLists) {
//            if (this.taskList.equals(listCtrl.getTaskList())) {
//                listCtrl.setTaskListName(newName);
//            }
//
        this.server.send("/app/taskList/edit", this.taskList);
        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
        this.name.setText(taskList.getName());
    }

    public void connectToWebSockets() {
        if (this.isConnected)
            return;
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
        this.isConnected = true;
    }
}
