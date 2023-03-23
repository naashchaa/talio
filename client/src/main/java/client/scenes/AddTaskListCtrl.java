package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

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
     * Adds a new instance of TaskList to the board overview and the database.
     */
    public void create() {
        try {
            this.server.addTaskList(this.getTaskList());
            this.boardCtrl.addTaskListToBoard();
        }
        catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            System.out.println(":(");
            e.printStackTrace();
        }
        this.name.clear();
        this.mainCtrl.showBoard();
    }

    /**
     * Creates a new instance of TaskList to be added in the create method with the user's input
     * @return a new instance of TaskList with the name the user introduced
     */
    public TaskList getTaskList() {
        Board b = new Board("test");
        return new TaskList(this.name.getText(), b);
    }
}
