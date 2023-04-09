package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * AddTaskList is currently connected to MainCtrl, but
 * the board is done will be connected to board and task.
 */
public class AddTaskListCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private BoardCtrl parentBoardCtrl;
    private boolean isConnected;
    @FXML
    private TextField name;


    @Inject
    public AddTaskListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isConnected = false;
    }

    public void setParentBoardCtrl(BoardCtrl ctrl) {
        this.parentBoardCtrl = ctrl;
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
        TaskList tasklist = new TaskList(this.name.getText(),
                this.parentBoardCtrl.getBoard());
        try {
            //tasklist = this.server.addTaskList(tasklist);
            this.server.send("/app/tasklists/add", tasklist);
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
}
