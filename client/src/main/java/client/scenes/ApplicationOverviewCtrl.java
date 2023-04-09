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
import java.util.*;

public class ApplicationOverviewCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Map<Board, Pair<BoardCtrl, Parent>> boards;
    private Map<Board, Pair<BoardPreviewCtrl, Parent>> boardPreviews;
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
        this.mainCtrl.showJoinBoard();
    }

    public void adminLogin() {
        this.mainCtrl.showAdminLogin();
    }

    /**
     * Adds the preview of a board in the list containing the user's boards.
     * @param name - the name of the board
     * @param board - the board associated with that preview
     */
    public void addBoardPreview(String name, Board board) {
        Pair<BoardPreviewCtrl, Parent> pair;
        if (this.mainCtrl.isAdmin()){
            pair = (Main.FXML.load(BoardPreviewCtrl.class,
                            "client", "scenes", "AdminBoardPreview.fxml"));
        }
        else {
            pair = (Main.FXML.load(BoardPreviewCtrl.class,
                            "client", "scenes", "BoardPreview.fxml"));
        }
        pair.getKey().setName(name);
        this.boardList.getChildren().add(pair.getValue());
        this.boardPreviews.put(board, pair);
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
        this.boards.get(board).getKey().loadTaskLists();
    }

    /**
     * Loads the previews of the existing boards.
     */
    public void loadExistingBoards(){
        this.boardList.getChildren().clear();
        this.boardPreviews.clear();
        this.boards.clear();
        for(Board board : this.server.getBoards()) {
            this.addBoardPreview(board.getName(), board);
            this.addBoard(board);
        }
    }

    public BoardCtrl getBoardCtrl(Board board) {
        return this.boards.get(board).getKey();
    }

    /** Removes the Ctrls from the list of boards and previews
     *  and puts them back in after editing the name.
     * @param board - the board to be edited
     * @param newName - the new name of the board
     */
    public void editBoard(Board board, String newName) {
        var pair1 = this.boardPreviews.get(board);
        var pair2 = this.boards.get(board);
        pair1.getKey().editLabel(newName);
        pair2.getKey().setName(newName);
        this.boardPreviews.remove(board);
        this.boards.remove(board);
        board.setName(newName);
        this.boardPreviews.put(board, pair1);
        this.boards.put(board, pair2);
        this.server.editBoard(board);
    }

    public void deleteBoard(Board board) {
        this.boardList.getChildren().remove(this.boardPreviews
                .get(board).getValue());
        this.boardPreviews.remove(board);
        this.boards.remove(board);
    }

    public void stop() {
        this.server.stop();
    }
}
