package client.scenes;

import client.Main;
import client.services.BoardService;
import client.services.TaskListService;
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
    private final Map<Board, Pair<BoardCtrl, Parent>> boards;
    private final Map<Board, Pair<BoardPreviewCtrl, Parent>> boardPreviews;
    private final BoardService boardService;
    private final TaskListService listService;
    @FXML
    private VBox boardList;
    @FXML
    private AnchorPane boardDisplay;

    /** The constructor for ApplicationOverviewCtrl.
     * @param server The ServerUtils instance
     * @param mainCtrl the MainCtrl instance
     * @param boardService the BoardService instance
     * @param listService the TaskListService instance
     */
    @Inject
    public ApplicationOverviewCtrl(ServerUtils server, MainCtrl mainCtrl,
                                   BoardService boardService, TaskListService listService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boards = new HashMap<>();
        this.boardPreviews = new HashMap<>();
        this.boardService = boardService;
        this.listService = listService;
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
     * @param ctrl - the board controller associated with that preview
     */
    public void addBoardPreview(String name, BoardCtrl ctrl) {
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
        pair.getKey().setParentCtrl(this);
        pair.getKey().setBoardCtrl(ctrl);
        this.boardList.getChildren().add(pair.getValue());
        this.boardPreviews.put(ctrl.getBoard(), pair);
        this.updateContext();
        this.mainCtrl.hidePopUp();
    }

    public void updateContext() {
        // make a list of remembered boards
        List<Board> boards = new LinkedList<>(this.boardPreviews.keySet());

        // remember the change
        this.boardService.updateRememberedBoards(this.server.getConnectionString(), boards);
    }

    /**
     * Adds a new board and sets its corresponding
     * controllers, so they can later be accessed.
     * @param board - the board
     * @return the created board's controller
     */
    public BoardCtrl addBoard(Board board) {
        var pairBoard =
                (Main.FXML.load(BoardCtrl.class, "client", "scenes", "Board.fxml"));
        this.boards.put(board, pairBoard);
        pairBoard.getKey().setBoard(board);
        pairBoard.getKey().setOverviewCtrl(this);
        pairBoard.getKey().setName(board.getName());
        return pairBoard.getKey();
    }

    /**
     * Loads on the screen the board associated with
     * the preview has been pressed from the list.
     * @param board - the board
     */
    public void loadBoardIntoOverview(Board board) {
        if (this.mainCtrl.getCurrentBoardCtrl() != null) {
            List<Node> taskListNodes =
                    this.mainCtrl.getCurrentBoardCtrl().getListContainer().getChildren();
            for (int i = 0; i < taskListNodes.size() - 1; i++) {
                TaskListCtrl listCtrl = (TaskListCtrl) taskListNodes.get(i).getUserData();
                if (listCtrl == null)
                    continue;
                listCtrl.removeTasks();
                this.listService.deleteTaskList(this.mainCtrl.getCurrentBoardCtrl(), listCtrl);
                listCtrl.disconnect();
            }
        }

        Node boardScene = this.boards.get(board).getValue();
        if(this.boardDisplay.getChildren().size() == 1) {
            this.boardDisplay.getChildren().remove(0);
        }
        this.boardDisplay.getChildren().add(boardScene);
        this.mainCtrl.updateBoard(this.boards.get(board).getKey());
        this.boards.get(board).getKey().setBoard(board);
        this.boards.get(board).getKey().loadContents();
    }

    /**
     * Loads the previews of the existing boards.
     */
    public void loadExistingBoards(){
        this.boardList.getChildren().clear();
        this.boardPreviews.clear();
        this.boards.clear();
        if(this.mainCtrl.isAdmin()){
            for (Board board: this.server.getBoards()){
                this.addBoardPreview(board.getName(), this.addBoard(board));
            }
            return;
        }
        for(Board board : this.server.getRememberedBoards()) {
            this.addBoardPreview(board.getName(), this.addBoard(board));
        }
    }

    /**
     * Removes a board from the list of boards.
     * @param board - the board to be removed
     */
    public void removeBoardPreview(Board board) {
        this.boardList.getChildren().clear();
        for(Board b : this.server.getBoards()) {
            if(this.boardPreviews.containsKey(b)) {
                this.addBoardPreview(b.getName(), this.addBoard(b));
            }
        }
        this.boardDisplay.getChildren().remove(this.boards.get(board).getValue());
        this.server.editBoard(board);
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
        this.removeBoardPreview(board);
        this.boards.remove(board);
    }

    public void stop() {
        this.server.stop();
    }
}
