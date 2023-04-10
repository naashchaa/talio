package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.inject.Inject;

public class LeaveBoardCtrl {

    private final MainCtrl mainCtrl;
    private BoardCtrl boardCtrl;
    private ApplicationOverviewCtrl appOverview;
    @FXML
    private Label boardName;

    @Inject
    public LeaveBoardCtrl(MainCtrl mainCtrl, ServerUtils server, ApplicationOverviewCtrl appOverview) {
        this.mainCtrl = mainCtrl;
        this.appOverview = appOverview;
    }

    public void cancel() {
        this.mainCtrl.hidePopUp();
    }

    public void confirm() {
        //TODO: method to remove the board from the user's view
        this.appOverview.removeBoardPreview(boardCtrl.getBoard());
        this.mainCtrl.hidePopUp();
    }

    public void setBoardCtrl(BoardCtrl bc) {
        this.boardCtrl = bc;
        this.boardName.setText(this.boardCtrl.getBoard().getName());
    }

}
