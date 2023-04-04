package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddTaskCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private TaskListCtrl parentTaskListCtrl;
    @FXML
    private TextField textField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;

    private boolean isConnected;

    @Inject
    public AddTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.isConnected = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        this.connectToWebSockets();
    }

    public void connectToWebSockets() {
        if (this.isConnected)
            return;
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
        this.isConnected = true;
    }

    /**
     * Cancels creating a task and goes back to the previous scene.
     * @param event An event triggered by user
     */
    public void cancelTask(ActionEvent event) {
        this.textField.clear();
        this.mainCtrl.hidePopUp();
    }

    /**
     * Creates a task and adds it to a task list.
     * @param event An event triggered by user
     */
    public void createTask(ActionEvent event) {
        Task task = new Task(this.textField.getText(), null,
                this.parentTaskListCtrl.getTaskList(), null);
//        try {
//            this.server.addTask(task);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        this.parentTaskListCtrl.addTaskToList(task.getName());

        // Sending the quote through the web socket
        this.server.send("/app/tasks/add", task);
        this.textField.clear();
        this.mainCtrl.hidePopUp();
    }

    public void setParentTaskListCtrl(TaskListCtrl parentTaskListCtrl) {
        this.parentTaskListCtrl = parentTaskListCtrl;
    }

    public TaskListCtrl getParentTaskListCtrl() {
        return this.parentTaskListCtrl;
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
