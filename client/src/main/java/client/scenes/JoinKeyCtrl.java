package client.scenes;

import com.google.inject.Inject;
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

    public void setFields(BoardCtrl ctrl) {
        this.key.setText(String.valueOf(ctrl.getBoard().getId()));
        this.boardName.setText("The Join Key for Board " + ctrl.getBoard().getName() + " is:");
    }

    public void cancel() {
        this.mainCtrl.hidePopUp();
    }
}
