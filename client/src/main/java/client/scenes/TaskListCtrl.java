package client.scenes;

import client.Main;
import client.services.BoardService;
import client.services.TaskListService;
import client.services.TaskService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TaskListCtrl extends Node implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final TaskListService listService;
    private final TaskService taskService;
    private final BoardService boardService;
    private boolean isConnected;
    private BoardCtrl parentCtrl;
    private TaskList taskList;
    @FXML
    private VBox taskContainer;
    @FXML
    private Label taskListName;
    @FXML
    private Pane highlightDrop;

    @Inject
    public TaskListCtrl(ServerUtils server, MainCtrl mainCtrl,
                        TaskService taskService, TaskListService listService,
                        BoardService boardService) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.taskService = taskService;
        this.listService = listService;
        this.boardService = boardService;
        this.isConnected = false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.connectToWebSockets();
        this.taskService.connectToWebSockets();
        // The task list ctrl gets subscribed to the following path.
        // Now it can receive updates that are sent back from the server to this path.
        this.setDragMethods();
    }

    /**
     * Registers for methods by subscribing to websockets.
     */
    public void registerForMessages() {
        this.server.registerForMessages("/topic/tasks/add", Task.class, task -> {
            System.out.println("notified");
            // makes sure that the parent task list is the only that shows task on client.
            if (this.taskList.getId() == task.getParentTaskList().getId()) {
                // this method is used to call runLater() to avoid JAVAFX thread errors.
                // this.loadTasksLater(); // this is causing an error because of remove method
                this.addTaskToListLater(task);
            }
        });
        this.server.registerForMessages("/topic/tasklists/edit", TaskList.class, taskList -> {
            if (this.taskList.getId() == taskList.getId()) {
                this.updateTaskListName(taskList);
            }
        });
        this.server.registerForMessages("/topic/tasklists/delete", TaskList.class, taskList -> {
            if (this.taskList.getId() == taskList.getId()) {
                this.removeThisCtrl();
            }
        });
        this.server.registerForMessages("/topic/tasks/drag", List.class, ids -> {
            System.out.println("should it?");
            if (((Long)((Integer)ids.get(0)).longValue()).equals(this.taskList.getId()) ||
                    ((Long)((Integer)ids.get(1)).longValue()).equals(this.taskList.getId())) {
                System.out.println("as it should");
                this.loadTasksLater();
            }
        });
    }

    public void loadTasks() {
        this.listService.loadTasks(this);
    }

    /**
     * When a task list is deleted, its associated controller should also be deleted.
     * It also should disappear from the children of the board.
     */
    public void removeThisCtrl() {
        Platform.runLater(() -> this.listService.deleteTaskList(this.parentCtrl, this));
    }

    public void updateTaskListName(TaskList taskList) {
        Platform.runLater(() -> {
            this.taskListName.setText(taskList.getName());
            this.setTaskList(taskList);
        });
    }

    /**
     * Helper method to be able to use runLater().
     */
    public void loadTasksLater() {
//        Platform.runLater(() -> {
//            this.loadTasksLaterHelper();
//        });
        Platform.runLater(() -> this.listService.loadTasks(this));
    }

//    /**
//     * Send a parent task list and a task to be added to that task list
//     * in the main ctrl.
//     */
//    public void loadTasksLaterHelper() {
//        this.removeTasks();
//        for(Task task : this.server.getTasksOfTasklist(this.taskList)) {
//            this.addTaskToList(task);
//        }
//        this.sortTasks();
//    }

    public void addTask() {
        this.mainCtrl.showAddTask(this);
    }

    public void addTaskToListLater(Task task) {
        Platform.runLater(() -> this.addTaskToList(task));
    }

    /** This method adds a certain task to the list so that it could be displayed.
     * @param task The task to add to the list
     */
    public void addTaskToList(Task task) {
        this.removeDummyPane();
        Pair<TaskCtrl, Parent> pair =
                Main.FXML.load(TaskCtrl.class, "client", "scenes", "Task.fxml");
        Label taskName = (Label) pair.getValue().lookup("#taskTitle");
        taskName.setText(task.getName());
        pair.getKey().setTask(task);
        pair.getKey().setParentCtrl(this);
        pair.getValue().setUserData(pair.getKey());
        this.taskContainer.getChildren().add(pair.getValue());
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public TaskList getTaskList() {
        return this.taskList;
    }

    public VBox getTaskContainer() {
        return this.taskContainer;
    }

    public BoardCtrl getParentCtrl() {
        return this.parentCtrl;
    }

    public void setParentCtrl(BoardCtrl ctrl) {
        this.parentCtrl = ctrl;
    }

    public void edit() {
        this.mainCtrl.showEditTaskList(this);
    }

    public void delete() {
        //this.server.deleteTaskListWrapper(this.taskList); // delete serverside
        //this.listService.deleteTaskList(this.getParentCtrl(), this); // delete clientside
        this.server.deleteTasksByParentList(this.taskList);
        this.server.send("/app/tasklists/delete", this.taskList);

    }

    public void showDeleteTaskList() {
        this.mainCtrl.showDeleteTaskList(this);
    }

    /**
     * Establishes a connection with the websockets if not already connected.
     */
    public void connectToWebSockets() {
        if (!this.isConnected) {
            this.server.establishWebSocketConnection();
            this.isConnected = true;
            this.registerForMessages();
            System.out.println("list ctrl connected to ws");
        }
    }

    /**
     * Removes all the nodes in a task list.
     */
    public void removeTasks() {
        List<Node> toRemove = new ArrayList<>();
        for (int i = 0; i < this.taskContainer.getChildren().size(); i++) {
            if (!this.taskContainer.getChildren().get(i).equals(this.highlightDrop)) {
                toRemove.add(this.taskContainer.getChildren().get(i));
                if (!((TaskCtrl)this.taskContainer.getChildren().
                        get(i).getUserData()).getIsConnected()) {
                    ((TaskCtrl)this.taskContainer.getChildren().get(i).getUserData()).disconnect();
                }
            }
        }
        this.taskContainer.getChildren().removeAll(toRemove);
    }
    public void disconnect() {
        if (this.isConnected) {
            this.server.terminateWebSocketConnection();
            this.isConnected = false;
            System.out.println("list ctrl disconnected from ws");
        }
    }

    public Pane getHighlightDrop() {
        return this.highlightDrop;
    }

    /** This method removes the dummy pane used for styling in drag and drop.
     * @return true if the pane was present and deleted, false otherwise
     */
    public boolean removeDummyPane() {
        if (this.taskContainer.getChildren().size() == 1 && this
            .taskContainer
            .getChildren()
            .get(0).equals(this.highlightDrop)) {
            this.taskContainer.getChildren().remove(0);
            return true;
        }
        return false;
    }

    public void addDummyPane() {
        if (this.taskContainer.getChildren().size() == 0){
            this.taskContainer.getChildren().add(this.getHighlightDrop());
            this.highlightDrop.setVisible(false);
        }
    }

    public void setDragMethods() {
        this.setDragOver();
        this.setDragEntered();
        this.setDragExited();
        this.setDragDropped();
    }

    /**
     * Sets drag over for this task list.
     */
    public void setDragOver() {
        VBox target = this.taskContainer;
        target.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });
    }

    /**
     * Sets the logic for when a drag and drop entity passes over this task list.
     */
    public void setDragEntered() {
        VBox target = this.taskContainer;
        target.setOnDragEntered(event -> {
            if (target.getChildren().size() == 1 &&
                target.getChildren().get(0).equals(TaskListCtrl.this.getHighlightDrop()))
                target.getChildren().get(0).setVisible(true);
        });
    }

    /**
     * Sets the logic for when a drag and drop exits this task list.
     */
    public void setDragExited() {
        VBox target = this.taskContainer;
        target.setOnDragExited(event -> {
            if (target.getChildren().size() == 1 &&
                target.getChildren().get(0).equals(TaskListCtrl.this.getHighlightDrop()))
                target.getChildren().get(0).setVisible(false);
        });
    }

    /**
     * Sets the logic for when a drag and drop is released over this task list.
     */
    public void setDragDropped() {
        VBox target = this.taskContainer;
        target.setOnDragDropped(event -> {
            TaskCtrl sourceTaskCtrl = (TaskCtrl)((Node)event.getGestureSource()).getUserData();
            TaskListCtrl targetList = TaskListCtrl.this;
            Long prevId = sourceTaskCtrl.getTask().getParentTaskList().getId();
            TaskListCtrl.this.taskService.moveTaskTo(sourceTaskCtrl, targetList, null);
            List<Long> ids = new ArrayList<>();
            ids.add(prevId);
            ids.add(targetList.getTaskList().getId());
            TaskListCtrl.this.server.send("/app/tasks/drag", ids);
            event.setDropCompleted(true);
            event.consume();
        });
    }

}
