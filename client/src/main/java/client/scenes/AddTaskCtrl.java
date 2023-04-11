package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.List;

public class AddTaskCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    @FXML
    public Button createButton;
    @FXML
    public Button cancelButton;
    private TaskListCtrl parentTaskListCtrl;
    @FXML
    private TextField textField;

    @Inject
    public AddTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }

    /**
     * Cancels creating a task and goes back to the previous scene.
     * @param event An event triggered by user
     */
    public void cancelTask(ActionEvent event) {
        this.textField.clear();
        this.mainCtrl.hidePopUp();
        event.consume();
    }

    /**
     * Creates a task and adds it to a task list.
     * @param event An event triggered by user
     */
    public void createTask(ActionEvent event) {
        Task task = new Task(this.textField.getText(), null,
                this.parentTaskListCtrl.getTaskList(), null);

        // tasks get put at the end of the list, therefore it has a null next
        // if it has a task behind it and should link to it with prev
        // the task before (if present) should have its next updated

        List<Node> nodes = this.parentTaskListCtrl.getTaskContainer().getChildren();
        long prev = nodes.size() == 0 ? 0 :
            (nodes.get(nodes.size() - 1).getUserData() == null ? 0 :
                (((TaskCtrl)nodes.get(nodes.size() - 1).getUserData()).getTask()).id);

        task.setPrevTask(prev);

        // Sending the task through the web socket
        this.server.send("/app/tasks/add", task);
        this.textField.clear();
        this.mainCtrl.hidePopUp();
        event.consume();
    }

    public void setParentTaskListCtrl(TaskListCtrl parentTaskListCtrl) {
        this.parentTaskListCtrl = parentTaskListCtrl;
    }

    public TaskListCtrl getParentTaskListCtrl() {
        return this.parentTaskListCtrl;
    }
}
