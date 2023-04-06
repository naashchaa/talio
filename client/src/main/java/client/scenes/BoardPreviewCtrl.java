package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class BoardPreviewCtrl {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private Board board;
    private ApplicationOverviewCtrl appOverview;
    @FXML
    private Label boardTitle;

    @Inject
    public BoardPreviewCtrl(ServerUtils server, MainCtrl mainCtrl,
                            ApplicationOverviewCtrl appOverview) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.appOverview = appOverview;
    }

    public void setName(String name) {
        this.boardTitle.setText(name);
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    public Board getBoard() {
        return this.board;
    }

    public void loadBoard() {
        this.appOverview.loadBoardIntoOverview(this.board);
    }
}
