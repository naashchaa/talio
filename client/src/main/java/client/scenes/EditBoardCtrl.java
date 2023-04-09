package client.scenes;

import javafx.fxml.FXML;
import com.google.inject.Inject;
import javafx.scene.control.TextField;

public class EditBoardCtrl {

    private final MainCtrl mainCtrl;
    private BoardCtrl ctrl;
    @FXML
    private TextField name;

    @Inject
    public EditBoardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void cancel() {
        this.mainCtrl.hidePopUp();
    }

    public void confirm(){
        //TODO: check if works correctly
        String newName = this.name.getText();
        this.ctrl.editBoard(newName);
        this.mainCtrl.hidePopUp();
    }

    public void setParentCtrl(BoardCtrl ctrl) {
        this.ctrl = ctrl;
        this.name.setText(ctrl.getBoard().getName());
    }
}
