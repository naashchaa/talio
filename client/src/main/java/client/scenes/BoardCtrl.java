package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;



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

    /**
     * Adds a new task list to the board.
     */
    public void addTaskListToBoard() {
        try{
            Pair<TaskListCtrl, Parent> pair =
                    Main.FXML.load(TaskListCtrl.class, "client", "scenes", "TaskList.fxml");
            Node button = this.container.getChildren().get(this.container.getChildren().size()-1);
            this.container.getChildren().
                    set(this.container.getChildren().size()-1, pair.getValue());
            this.container.getChildren().add(button);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the user into the newTask scene to create a new task.
     */
    public void addTask() {
        this.mainCtrl.showAddTask();
    }

    /**
     * Sends the user into the newTaskList scene to create a new task list.
     */
    public void addTaskList() {
        this.mainCtrl.showAddTaskList();

    }
}
