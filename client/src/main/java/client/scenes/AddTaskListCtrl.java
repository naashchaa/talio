package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.TaskList;
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

    private final BoardCtrl boardCtrl;
    @FXML
    private TextField name;


    @Inject
    public AddTaskListCtrl(ServerUtils server, MainCtrl mainCtrl, BoardCtrl boardCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardCtrl = boardCtrl;
    }

    /**
     * Undoes the action of creating a task list by deleting
     * the value of the textField filled by the user,
     * and returns to the overview.
     */
    public void cancel() {
        this.name.clear();
        this.mainCtrl.showBoard();
    }

    /**
     * When button in fxml file is pressed, a new tasks list is created and added
     * to the database.
     */
    public void create() {
       //ToDo: Method that creates a new task list with the name that the user inputs.
        // Needs endpoints.
        TaskList tasklist = new TaskList(this.name.getText(), this.mainCtrl.getCurrentBoard());
        try {
            this.server.addTaskList(tasklist);
            this.boardCtrl.addTaskListToBoard(tasklist.getName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.name.clear();
        this.mainCtrl.showBoard();
    }

    /**
     * Returns the tasks lists from the database if there are any.
     * @return the list of task lists.
     */
    public List<TaskList> getTaskLists() {
        try {
            return this.server.getTaskLists();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
