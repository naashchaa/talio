package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.inject.Inject;

//only admins should be able to delete boards
public class DeleteBoardCtrl {

    private final MainCtrl mainCtrl;
    private BoardCtrl boardCtrl;
    private ApplicationOverviewCtrl appOverview;
    @FXML
    private Label boardName;

    @Inject
    public DeleteBoardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
    }

    public void cancel() {
        this.mainCtrl.hidePopUp();
    }

    public void confirm() {
        //TODO: check if works correctly
        this.boardCtrl.deleteBoard();
        this.appOverview.deleteBoard(this.boardCtrl.getBoard());
        this.mainCtrl.hidePopUp();
    }

    public void setStuff(BoardCtrl bc, ApplicationOverviewCtrl appOverview) {
        this.boardCtrl = bc;
        this.boardName.setText(this.boardCtrl.getBoard().getName());
        this.appOverview = appOverview;
    }
}
