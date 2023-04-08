package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.*;

public class ApplicationOverviewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private HashMap<Board, Pair<BoardCtrl, Parent>> boards;
    private HashMap<Board, Pair<BoardPreviewCtrl, Parent>> boardPreviews;
    @FXML
    private VBox boardList;
    @FXML
    private AnchorPane boardDisplay;

    @Inject
    public ApplicationOverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boards = new HashMap<>();
        this.boardPreviews = new HashMap<>();
    }

    public void reconnect() {
        this.mainCtrl.showConnectToServer();
    }

    public void create() {
        this.mainCtrl.showCreateBoard();
    }
    public void join() {

    }

    /**
     * Adds the preview of a board in the list containing the user's boards.
     * @param name - the name of the board
     * @param board - the board associated with that preview
     */
    public void addBoardPreview(String name, Board board) {
        var pair =
                (Main.FXML.load(BoardPreviewCtrl.class, "client", "scenes", "BoardPreview.fxml"));
        pair.getKey().setName(name);
        this.boardList.getChildren().add(pair.getValue());
        pair.getKey().setBoard(board);
        this.mainCtrl.hidePopUp();
    }

    /**
     * Adds a new board and sets its corresponding
     * controllers, so they can later be accessed.
     * @param board - the board
     */
    public void addBoard(Board board) {
        var pairBoard =
                (Main.FXML.load(BoardCtrl.class, "client", "scenes", "Board.fxml"));
        this.boards.put(board, pairBoard);
        pairBoard.getKey().setBoard(board);
        pairBoard.getKey().setName(board.getName());
    }

    /**
     * Loads on the screen the board associated with
     * the preview has been pressed from the list.
     * @param board - the board
     */
    public void loadBoardIntoOverview(Board board) {
        Node boardScene = this.boards.get(board).getValue();
        if(this.boardDisplay.getChildren().size() == 1) {
            this.boardDisplay.getChildren().remove(0);
        }
        this.boardDisplay.getChildren().add(boardScene);
        this.mainCtrl.updateBoard(this.boards.get(board).getKey());
        this.boards.get(board).getKey().setBoard(board);
        this.boards.get(board).getKey().loadTaskLists();
    }

    /**
     * Loads the previews of the existing boards.
     */
    public void loadExistingBoards(){
        for(Board board : this.server.getBoards()) {
            this.addBoardPreview(board.getName(), board);
            this.addBoard(board);
        }
    }


    public void stop() {
        this.server.stop();
    }
}
