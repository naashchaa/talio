package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddTaskCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
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

    public Task getTask() {
//        var t = this.textField.getText();
//        Board board = new Board("test board");
//        TaskList taskList = new TaskList("test tasklist", board);
//        return new Task(t, "test description", taskList, null);
        // return new Task(this.textField, null, )
        return null;
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

        System.out.println("creating task");
        try {
            this.server.addTask(this.getTask());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.textField.clear();
        this.mainCtrl.showBoard();
    }
}
