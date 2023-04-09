package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * AddTaskList is currently connected to MainCtrl, but
 * the board is done will be connected to board and task.
 */
public class AddTaskListCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private boolean isConnected;
    @FXML
    private TextField name;


    @Inject
    public AddTaskListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isConnected = false;
    }

    /**
     * Undoes the action of creating a task list by deleting
     * the value of the textField filled by the user,
     * and returns to the overview.
     */
    public void cancel() {
        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    /**
     * When button in fxml file is pressed, a new tasks list is created and added
     * to the database.
     */
    public void create() {
        Platform.runLater(this::createHelper);
    }

    /**
     * This helped is created to be able to run later so that there are not JAVAFX thread errors.
     */
    public void createHelper() {
        BoardCtrl boardCtrl = this.mainCtrl.getCurrentBoardCtrl();
        TaskList tasklist = new TaskList(this.name.getText(),
                boardCtrl.getBoard());
        //this.server.send("/app/taskList", tasklist);
        try { // might need a platform.runlater
            //this.server.addTaskList(tasklist);
            //tasklist = this.server.getTaskList(tasklist); // delete if not work!
            this.server.send("/app/taskList/add", tasklist);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.name.clear();
        this.mainCtrl.hidePopUp();
    }

    public void connectToWebSockets() {
        if (this.isConnected)
            return;
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
        this.isConnected = true;
    }

    /**
     * Returns the tasks lists from the database if there are any.
     * @return the list of task lists.
     */
    public List<TaskList> getTaskLists() {
        try {
            var lists = this.server.getTaskLists();
            return lists;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
