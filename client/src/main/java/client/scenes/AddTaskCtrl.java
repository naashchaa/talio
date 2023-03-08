package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddTaskCtrl {

    @FXML
    private TextField textField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;

    public void cancelTask(ActionEvent event) {
        System.out.println("cancelling task");
    }

    public void createTask(ActionEvent event) {
        System.out.println("creating task");
    }
}
