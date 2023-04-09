package client.scenes;

import client.Main;
import client.services.BoardService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.TaskList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for board.
 */
public class BoardCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ApplicationOverviewCtrl ctrl;
    private final BoardService boardService;
    private Board board;
    @FXML
    private Label boardName;
    @FXML
    private HBox container;

    /** Initializes the board controller and starts polling for updates.
     * @param server server utils instance
     * @param mainCtrl main controller instance
     * @param boardService the service class for board logic
     */
    @Inject
    public BoardCtrl(ServerUtils server, MainCtrl mainCtrl, BoardService boardService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.boardService = boardService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.server.registerForTaskListsL(this::addTaskListToBoard);
    }

    public void setOverviewCtrl(ApplicationOverviewCtrl ctrl) {
        this.ctrl = ctrl;
    }

    public void loadContentsLater() {
        Platform.runLater(this::loadContents);
    }

    public void loadContents() {
        this.boardService.loadContents(this);
    }

    public void refreshLater() {
        this.boardService.refreshBoardLater(this);
    }

//    public void refreshTaskListLater(BoardCtrl ctrl, TaskListCtrl list) {
//        this.boardService.refreshTaskListLater(ctrl, list);
//        this.connectToWebSockets();
//        this.server.registerForMessages("/topic/tasklists/add", TaskList.class, taskList -> {
//            Long l1 = taskList.getParentBoard().getId();
//            Long l2 = this.board.getId();
//            if (l1.equals(l2)) {
//                this.addTaskListToBoard(taskList);
//            }
//        });
//    }

    /**
     * Find the correct node that was previously removed and deletes it.
     * @param taskList the task list which is equal to the node user data.
     */
    public void removeTaskList(TaskList taskList) {
        for (int i = 0; i < this.container.getChildren().size() - 1; i++) {
            if (this.container.getChildren().get(i).getUserData().equals(taskList.getId())) {
                this.container.getChildren().remove(i);
                break;
            }
        }
    }

    /** Adds a new task list to the board.
     * @param taskList - corresponding taskList instance
     * @return returns a controller for the task list
     */
    public TaskListCtrl addTaskListToBoard(TaskList taskList) {
        try{
            var pair = (Main.FXML.load(TaskListCtrl.class, "client", "scenes", "TaskList.fxml"));
            Label label = (Label) pair.getValue().lookup("#taskListName");
            label.setText(taskList.getName());
            pair.getKey().setTaskList(taskList);
            pair.getKey().setParentCtrl(this);
            pair.getValue().setUserData(pair.getKey());
            Node button = this.container.getChildren().get(this.container.getChildren().size()-1);
            this.container.getChildren().
                    set(this.container.getChildren().size()-1, pair.getValue());
            this.container.getChildren().add(button);
            pair.getValue().setUserData(pair.getKey());
            // might ruin it
            pair.getKey().loadTasksLater();
            return pair.getKey();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Loads the task list as the board is opened.
     */
//    public void loadTaskLists() {
//        this.removeTaskLists();
//        List<TaskList> lists = this.server.getTaskListOfBoard(this.board);
//        for(TaskList taskList: lists) {
//            this.mainCtrl.addToTaskListCtrl(this.addTaskListToBoard(taskList));
//        }
//    }

    /**
     * Sends the user into the newTaskList scene to create a new task list.
     */
    public void addTaskList() {
        this.mainCtrl.showAddTaskList();
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ApplicationOverviewCtrl getParentCtrl() {
        return this.ctrl;
    }

    public HBox getListContainer() {
        return this.container;
    }

    /**
     * Removes all children in horizontal box but the button.
     */
    public void removeTaskLists() {
        this.boardService.removeTaskLists(this);
    }

    public void editBoard(String newName) {
        this.ctrl.editBoard(this.board, newName);
    }

    public void deleteBoard() {
        //TODO: check if works correctly
        this.server.deleteEverythingOfBoard(this.board);
        this.server.deleteBoard(this.board);
    }

    public void refresh() {
        this.boardService.loadTaskLists(this);
    }

    public void setName(String name){
        this.boardName.setText(name);
    }

    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }

    public void getJoinKey() {
        this.mainCtrl.showJoinKey(this);
    }
}
