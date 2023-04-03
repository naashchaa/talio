package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class DeleteTaskListCtrl {

    private final MainCtrl mainCtrl;
    private TaskListCtrl taskListCtrl;
    @FXML
    private Label taskListName;

    @Inject
    public DeleteTaskListCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Cancels the modification of the task list.
     */
    public void cancel() {
        this.mainCtrl.hidePopUp();
    }

    /**
     * Takes the user's input passes it to the database and
     * then loads the new name in the task list controller.
     */
    public void confirm() {
        this.taskListCtrl.delete();
        this.mainCtrl.hidePopUp();
    }

    public void setTaskListCtrl(TaskListCtrl tlc) {
        this.taskListCtrl = tlc;
        this.taskListName.setText(this.taskListCtrl.getTaskList().getName());
    }
}