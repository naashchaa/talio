package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ConnectToServerCtrl {

    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    @FXML
    private TextField textField;

    @FXML private Text error;

    @Inject
    public ConnectToServerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /** Saves the connection string and tries to update.
     * @param event An event triggered by user
     */
    public void connect(ActionEvent event) {
        if (!this.server.setConnectionString(this.textField.getText())) {
            this.error.setVisible(true);
            return;
        }

        this.error.setVisible(false);
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
        this.mainCtrl.loadBoard();
        this.mainCtrl.loadTaskListsHelper();
        this.mainCtrl.showBoard();
    }
}
