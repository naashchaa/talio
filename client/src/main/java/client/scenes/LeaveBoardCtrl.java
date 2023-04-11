package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.inject.Inject;

public class LeaveBoardCtrl {

    private final MainCtrl mainCtrl;
    private ApplicationOverviewCtrl appCtrl;
    private BoardCtrl boardCtrl;
    @FXML
    private Label boardName;

    @Inject
    public LeaveBoardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setParentCtrl(ApplicationOverviewCtrl ctrl) {
        this.appCtrl = ctrl;
    }

    public void cancel() {
        this.mainCtrl.hidePopUp();
    }

    public void confirm() {
        this.appCtrl.deleteBoard(this.boardCtrl.getBoard());
        this.appCtrl.updateContext();
        this.mainCtrl.hidePopUp();
    }

    public void setBoardCtrl(BoardCtrl bc) {
        this.boardCtrl = bc;
        this.boardName.setText(bc.getBoard().getName());
    }

}
