package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.io.IOException;

/**
 * AddTaskList is currently connected to MainCtrl, but
 * the board is done will be connected to board and task.
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
     * Undoes the action of creating a task list by deleting
     * the value of the textField filled by the user,
     * and returns to the overview.
     */
    public void cancel() {
        this.name.clear();
        this.mainCtrl.showBoard();
    }

    public void create() {
       try {
           //adds task list to database
           this.server.addTaskList(getTaskList());
           //adds task list to board scene
           try {
               TaskListCtrl.initialize();
           } catch (IOException e) {
               System.out.println("here");
               e.printStackTrace();
           }

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

    public TaskList getTaskList() {
        Board b = new Board("test");
        return new TaskList(name.getText(), b);
    }
}
