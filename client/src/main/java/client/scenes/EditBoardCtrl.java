package client.scenes;

import commons.Board;
import javafx.fxml.FXML;
import com.google.inject.Inject;
import javafx.scene.control.TextField;

public class EditBoardCtrl {

    private final MainCtrl mainCtrl;
    private ApplicationOverviewCtrl appOverview;
    private Board board;
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
        this.appOverview.editBoard(this.board, newName);
        this.mainCtrl.hidePopUp();
    }

    public void setStuff(Board board, ApplicationOverviewCtrl appOverview) {
        this.board = board;
        this.name.setText(board.getName());
        this.appOverview = appOverview;
    }
}
