package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
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

    public void cancelTask(ActionEvent event) {
        System.out.println("cancelling task");
        mainCtrl.showBoard();
    }

    public void createTask(ActionEvent event) {
        System.out.println("creating task");
    }
}
