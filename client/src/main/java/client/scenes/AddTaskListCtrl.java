package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * AddTaskList is currently connected to MainCtrl, but after the board is done will be connected to board and task.
 */
public class AddTaskListCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField name;


    @Inject
    public AddTaskListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Undoes the action of creating a task list by deleting the value of the textField filled by the user,
     * and returns to the overview.
     */
    public void cancel() {
        name.clear();
        mainCtrl.showBoard();
    }

    public void create() {
       //ToDo: Method that creates a new task list with the name that the user inputs.
        // Needs endpoints.
    }

}
