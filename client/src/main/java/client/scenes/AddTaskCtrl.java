package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class AddTaskCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private TaskListCtrl parentTaskListCtrl;
    @FXML
    private TextField textField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;

    @Inject
    public AddTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Cancels creating a task and goes back to the previous scene.
     * @param event An event triggered by user
     */
    public void cancelTask(ActionEvent event) {
        System.out.println("cancelling task");
        this.mainCtrl.showBoard();
    }

    /**
     * Creates a task and adds it to a task list.
     * @param event An event triggered by user
     */
    public void createTask(ActionEvent event) {
        Task task = new Task(this.textField.getText(), null,
                this.parentTaskListCtrl.getTaskList(), null);
        try {
            this.server.addTask(task);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.parentTaskListCtrl.addTaskToList(task.getName());
        this.textField.clear();
        this.mainCtrl.showBoard();
    }

    public void setParentTaskListCtrl(TaskListCtrl parentTaskListCtrl) {
        this.parentTaskListCtrl = parentTaskListCtrl;
    }

    /**
     * Gets the tasks associated to a task list and returns them.
     * @param tasklist the parent task list the tasks are associated to.
     * @return a list of tasks.
     */
    public List<Task> getTasks(TaskList tasklist) {
        try {
            return this.server.getTasks(tasklist);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
