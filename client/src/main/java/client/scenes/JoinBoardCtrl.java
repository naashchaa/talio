package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class JoinBoardCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private ApplicationOverviewCtrl ctrl;
    @FXML
    private TextField boardId;
    @FXML
    private Text wrongId;

    @Inject
    public JoinBoardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void setParentOverviewCtrl(ApplicationOverviewCtrl ctrl) {
        this.ctrl = ctrl;
    }

    public void cancel() {
        this.wrongId.setVisible(false);
        this.mainCtrl.hidePopUp();
    }

    /**
     * checks if the board exists and joins it if it does.
     */
    public void join(){
        long id = Long.parseLong(this.boardId.getText());
        if (this.server.checkBoardExists(id)){
            this.wrongId.setVisible(false);

            Board newBoard = this.server.getBoard(Long.parseLong(this.boardId.getText()));
            this.ctrl.addBoardPreview(newBoard.getName(), this.ctrl.addBoard(newBoard));

            this.boardId.clear();
            this.mainCtrl.hidePopUp();
        }
        else {
            this.boardId.clear();
            this.wrongId.setVisible(true);
        }
    }
}
