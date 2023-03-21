package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.util.List;

public class BoardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox container;

    @Inject
    public BoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void addTask() {
        this.mainCtrl.showAddTask();
    }

//        //object TaskList takes the parameters introduced by the user
//        and creates a new instance with them
//        TaskListCtrl taskList = new TaskListCtrl(server, mainCtrl, "hello");
//        //the TaskList is added to the list of children of the HBox
//        container.getChildren().add(taskList);
    public void addTaskList() {
        this.mainCtrl.showAddTaskList();
    }

    /**
     * Returns the first of the list of boards from the database.
     * If the database has no boards, it creates one with the name "Default Board".
     * @return returns the board from the database.
     */
    public Board getBoard() {
        try {
            List<Board> boards = this.server.getBoard();
            if (boards.size() == 0) {
                Board newBoard = new Board("Default Board");
                this.server.addBoard(newBoard);
                return this.server.getBoard().get(0);
            }
            return boards.get(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
