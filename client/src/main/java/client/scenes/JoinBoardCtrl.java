package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class JoinBoardCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
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
        if (this.server.checkBoardExists((long) Long.parseLong(this.boardId.getText()))){
            this.wrongId.setVisible(false);
            this.boardId.clear();
            this.mainCtrl.hidePopUp();
        }
        else {
            this.boardId.clear();
            this.wrongId.setVisible(true);
        }
    }
}
