package client.scenes;

import client.services.TaskService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TaskCtrl extends Node implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final TaskService service;
    private TaskListCtrl parentCtrl;

    private Task task;
    @FXML
    private BorderPane taskContainer;
    @FXML
    private HBox contentContainer;
    @FXML
    private Label taskTitle;
    @FXML
    private Pane taskTop;
    @FXML
    private Pane taskBottom;

    @Inject
    public TaskCtrl(ServerUtils server, MainCtrl mainCtrl, TaskService service, String title) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.service = service;
        this.taskTitle = new Label(title);
    }

    /** This method initializes task controller.
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.connectToWebSockets();
        this.service.connectToWebSockets();
        // Subscribing the task controller to the edit path
        this.server.registerForMessages("/topic/tasks/edit", Task.class, task -> {
            if (task.id == this.task.id) {
                this.setTask(task);
                System.out.println("received an edit notification");
                this.service.refreshTaskLater(this);
            }
        });
        // Subscribing the task controller to the delete path
        this.server.registerForMessages("/topic/tasks/delete", Task.class, task -> {
            if (task.id == this.task.id) {
                this.service.deleteTaskLater(this.parentCtrl, task);
            }
        });
        this.server.registerForMessages("/topic/tasks/drag", List.class, ids -> {
            System.out.println("received an update");
            if (((Long) ((Integer) ids.get(0)).longValue()).equals(this.task.id)) {
                System.out.println("received a drag update for this controller");
                // check if drag already happened
                if (!this.service.isAlreadyDragged(this, ids)) {
                    System.out.println("detected to not be dragged yet");
                    List<Object> list = this.service.getDragParameters(this, ids);
                    this.service.moveTaskTo((TaskCtrl)list.get(0),
                        (TaskListCtrl)list.get(1),
                        (Task)list.get(2));
                    System.out.println("executed drag");
                }
            }

        });
        // set on drag stuff
        this.setDragStuff();
    }

    public TaskListCtrl getParentCtrl() {
        return this.parentCtrl;
    }

    public void setParentCtrl(TaskListCtrl ctrl) {
        this.parentCtrl = ctrl;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TaskCtrl getNextTask(Task task) {
        return this.service.getNextTask(this.parentCtrl, task);
    }

    public void moveTaskTo(TaskCtrl sourceTaskCtrl,
                           TaskListCtrl targetListCtrl, Task nextTask) {
        this.service.moveTaskTo(sourceTaskCtrl, targetListCtrl, nextTask);
    }

    public void edit() {
        this.mainCtrl.showEditTask(this);
    }

    /**
     * Deletes a task from the server and removes it from the task list.
     */
    public void delete() {
        this.server.send("/app/tasks/delete", this.task);
    }

    public void openTaskOverview() {
        System.out.println("Opening task overview for task " + this.taskTitle.getText());
    }

    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }

    public void disconnect() {
        this.server.terminateWebSocketConnection();
    }

    /**
     * This method initializes all the drag and drop related properties.
     */
    public void setDragStuff() {
        this.setDragDetected();
        this.setDragOver();
        this.setDragEntered();
        this.setDragExited();
        this.setDragDropped();
        this.setDragDone();
    }

    /**
     * Makes the task able to be dragged.
     */
    public void setDragDetected() {
        BorderPane source = this.taskContainer;
        HBox content = this.contentContainer;
        source.setOnDragDetected(event -> {
            Dragboard db = source.startDragAndDrop(TransferMode.ANY);
            ClipboardContent cc = new ClipboardContent();
            cc.putString("Something");
            db.setContent(cc);
            source.setBackground(new Background(
                    new BackgroundFill(
                            Color.rgb(220,168,124), CornerRadii.EMPTY, Insets.EMPTY)));
            content.setBackground(new Background(
                    new BackgroundFill(
                            Color.rgb(220,168,124), CornerRadii.EMPTY, Insets.EMPTY)));
            event.consume();
        });
    }

    /**
     * Sets drag over detected for top part of task and bottom part of task.
     */
    public void setDragOver() {
        Pane topTarget = this.taskTop;
        topTarget.setOnDragOver(event -> {
            if (event.getGestureSource() != topTarget.getParent()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        Pane bottomTarget = this.taskBottom;

        bottomTarget.setOnDragOver(event -> {
            if (event.getGestureSource() != bottomTarget.getParent()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    /**
     * Visual feedback set when a node is dragged over the top pane or bottom pane.
     */
    public void setDragEntered() {
        Pane topTarget = this.taskTop;
        topTarget.setOnDragEntered(event -> {
            if (event.getGestureSource() != topTarget.getParent()) {
                topTarget.setBackground(new Background(
                        new BackgroundFill(
                                Color.rgb(10, 125, 125), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
        Pane bottomTarget = this.taskBottom;
        bottomTarget.setOnDragEntered(event -> {
            if (event.getGestureSource() != bottomTarget.getParent()) {
                bottomTarget.setBackground(new Background(
                        new BackgroundFill(
                                Color.rgb(10, 125, 125), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
    }

    /**
     * Sets back the color of the visual feedback to what it was before.
     */
    public void setDragExited() {
        Pane topTarget = this.taskTop;
        topTarget.setOnDragExited(event -> {
            if (event.getGestureSource() != topTarget.getParent()) {
                topTarget.setBackground(new Background(
                        new BackgroundFill(
                                Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
        Pane bottomTarget = this.taskBottom;
        bottomTarget.setOnDragExited(event -> {
            if (event.getGestureSource() != bottomTarget.getParent()) {
                bottomTarget.setBackground(new Background(
                        new BackgroundFill(
                                Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        });
    }

    /**
     * This method specifies the logic to be executed
     * when a drag and drop element is released over this task.
     */
    public void setDragDropped() {
        // do the magic here
        Pane topTarget = this.taskTop;

        topTarget.setOnDragDropped(event -> {
            boolean success = false;
            System.out.println("dropped on the top");
            if (event.getGestureSource() != topTarget.getParent()) {
                TaskCtrl sourceTaskCtrl = (TaskCtrl)((Node)event.getGestureSource()).getUserData();
                TaskCtrl nextTask = (TaskCtrl)(topTarget.getParent()).getUserData();
                TaskListCtrl targetList = nextTask.getParentCtrl();
                this.setParentCtrl(targetList);

                Long prevId = sourceTaskCtrl.getTask().getParentTaskList().getId();
                TaskCtrl.this.moveTaskTo(sourceTaskCtrl, targetList, nextTask.getTask());
                success = true;
                List<Long> ids = new ArrayList<>();
                ids.add(prevId);
                ids.add(targetList.getTaskList().getId());
                TaskCtrl.this.server.send("/app/tasks/drag", ids);
            }
            event.setDropCompleted(success);
            event.consume();
        });

        Pane bottomTarget = this.taskBottom;

        bottomTarget.setOnDragDropped(event -> {
            boolean success = false;
            if (event.getGestureSource() != bottomTarget.getParent()) {
                TaskCtrl sourceTaskCtrl = (TaskCtrl)((Node)event.getGestureSource()).getUserData();
                TaskCtrl prevTaskCtrl = (TaskCtrl)(bottomTarget.getParent()).getUserData();

                TaskCtrl nextTaskCtrl = TaskCtrl.this.getNextTask(prevTaskCtrl.getTask());
                Task nextTask = nextTaskCtrl == null ? null : nextTaskCtrl.getTask();
                if (sourceTaskCtrl.getTask().equals(nextTask)) {
                    event.setDropCompleted(false);
                    event.consume();
                    return;
                }

                TaskListCtrl targetList = prevTaskCtrl.getParentCtrl();

                Long prevId = sourceTaskCtrl.getTask().getParentTaskList().getId();
                TaskCtrl.this.moveTaskTo(sourceTaskCtrl, targetList, nextTask);
                success = true;
                List<Long> ids = new ArrayList<>();
                ids.add(prevId);
                ids.add(targetList.getTaskList().getId());
                TaskCtrl.this.server.send("/app/tasks/drag", ids);
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Set back the visual feedback of the object being dragged when done.
     */
    public void setDragDone() {
        BorderPane source = this.taskContainer;
        HBox content = this.contentContainer;
        source.setOnDragDone(event -> {
            source.setBackground(new Background(
                    new BackgroundFill(
                            Color.rgb(232, 213, 196), CornerRadii.EMPTY, Insets.EMPTY)));
            content.setBackground(new Background(
                    new BackgroundFill(
                            Color.rgb(232, 213, 196), CornerRadii.EMPTY, Insets.EMPTY)));
            event.consume();
        });
    }
}
