package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class CreateBoardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ApplicationOverviewCtrl appOverview;

    @FXML
    private TextField boardTitle;


    @Inject
    public CreateBoardCtrl(ServerUtils server, MainCtrl mainCtrl,
                           ApplicationOverviewCtrl appOverview) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.appOverview = appOverview;
    }

    public void cancel() {
        this.boardTitle.clear();
        this.mainCtrl.hidePopUp();
    }

    public void create() {
        Board newBoard = new Board(this.boardTitle.getText());
        newBoard = this.server.addBoard(newBoard);
        BoardCtrl ctrl = this.appOverview.addBoard(newBoard);
        this.appOverview.addBoardPreview(this.boardTitle.getText(), ctrl);
        this.boardTitle.clear();
    }
}
