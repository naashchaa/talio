package client.scenes;

import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class JoinKeyCtrl {

    private final MainCtrl mainCtrl;
    @FXML
    private Label key;
    @FXML
    private Label boardName;

    @Inject
    public JoinKeyCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setFields(Board board) {
        this.key.setText(String.valueOf(board.getId()));
        this.boardName.setText("The Join Key for Board " + board.getName() + " is:");
    }

    public void cancel() {
        this.mainCtrl.hidePopUp();
    }
}
