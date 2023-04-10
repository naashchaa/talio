package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class BoardPreviewCtrl {
    private final MainCtrl mainCtrl;
    private ApplicationOverviewCtrl parentCtrl;
    private BoardCtrl boardCtrl;
    @FXML
    private Label boardTitle;

    @Inject
    public BoardPreviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setName(String name) {
        this.boardTitle.setText(name);
    }

    public void setParentCtrl(ApplicationOverviewCtrl ctrl) {
        this.parentCtrl = ctrl;
    }

    public void setBoardCtrl(BoardCtrl ctrl) {
        this.boardCtrl = ctrl;
    }

    public void loadBoard() {
        this.parentCtrl.loadBoardIntoOverview(this.boardCtrl.getBoard());
    }

    public void delete() {
        this.mainCtrl.showDeleteBoard(this.boardCtrl);
    }

    public void edit() {
        this.mainCtrl.showEditBoard(this.boardCtrl);
    }

    public void remove() {
        this.mainCtrl.showRemoveBoard(this.boardCtrl);
    }

    public void editLabel(String name) {
        this.boardTitle.setText(name);
    }
}
