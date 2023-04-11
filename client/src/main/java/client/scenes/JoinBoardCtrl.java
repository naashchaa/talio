package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class JoinBoardCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private ApplicationOverviewCtrl appOverview;
    @FXML
    private TextField boardId;
    @FXML
    private Text wrongId;

    @Inject
    public JoinBoardCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = new ServerUtils();
    }

    public void cancel() {
        this.wrongId.setVisible(false);
        this.mainCtrl.hidePopUp();
    }

    /**
     * checks if the board exists and joins it if it does.
     */
    public void join(){
        //TODO: check if works correctly and implement method to join the board
        long id = Long.parseLong(this.boardId.getText());
        if (this.server.checkBoardExists(id)){
            this.wrongId.setVisible(false);
            this.boardId.clear();
            this.appOverview.joinBoardPreview(id);
            this.mainCtrl.hidePopUp();
        }
        else {
            this.boardId.clear();
            this.wrongId.setVisible(true);
        }
    }

    public void setCtrls(ApplicationOverviewCtrl appOverview) {
        this.appOverview = appOverview;
    }
}
