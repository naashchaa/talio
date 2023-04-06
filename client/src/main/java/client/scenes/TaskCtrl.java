package client.scenes;

import client.services.TaskService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskCtrl extends Node implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final TaskService service;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.connectToWebSockets();
        this.service.connectToWebSockets();
        // Subscribing the task controller to the edit path
        this.server.registerForMessages("/topic/tasks/edit", Task.class, task -> {
            if (task.id == this.task.id) {
                this.mainCtrl.refreshTaskLater(task);
            }
        });
        // Subscribing the task controller to the delete path
        this.server.registerForMessages("/topic/tasks/delete", Task.class, task -> {
            if (task.id == this.task.id) {
                this.mainCtrl.deleteTaskLater(task);
            }
        });
        // set on drag stuff
        this.setDragStuff();
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getNextTask(Task task) {
        return this.service.getNextTask(task);
    }

    public void moveTaskTo(Task taskToMove, TaskList listToMoveTo, Task nextTask) {
        this.service.moveTaskTo(taskToMove, listToMoveTo, nextTask);
    }


    public void edit() {
        this.mainCtrl.showEditTask(this.task);
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

    /**
     * This method initializes all of the drag and drop related properties.
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
     * Makes the task be able to be dragged.
     */
    public void setDragDetected() {
        BorderPane source = this.taskContainer;

        HBox content = this.contentContainer;
        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
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
            }
        });
    }

    /**
     * Sets drag over detected for top part of task and bottom part of task.
     */
    public void setDragOver() {
        Pane topTarget = this.taskTop;
        topTarget.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {

                if (event.getGestureSource() != topTarget.getParent()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        Pane bottomTarget = this.taskBottom;
        bottomTarget.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != bottomTarget.getParent()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
    }

    /**
     * Visual feedback set when a node is dragged over the top pane or bottom pane.
     */
    public void setDragEntered() {
        Pane topTarget = this.taskTop;
        topTarget.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != topTarget.getParent()) {
                    topTarget.setBackground(new Background(
                            new BackgroundFill(
                                    Color.rgb(10, 125, 125), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        });
        Pane bottomTarget = this.taskBottom;
        bottomTarget.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != bottomTarget.getParent()) {
                    bottomTarget.setBackground(new Background(
                            new BackgroundFill(
                                    Color.rgb(10, 125, 125), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        });
    }

    /**
     * Sets back the color of the visual feedback to what it was before.
     */
    public void setDragExited() {
        Pane topTarget = this.taskTop;
        topTarget.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != topTarget.getParent()) {
                    topTarget.setBackground(new Background(
                            new BackgroundFill(
                                    Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        });
        Pane bottomTarget = this.taskBottom;
        bottomTarget.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != bottomTarget.getParent()) {
                    bottomTarget.setBackground(new Background(
                            new BackgroundFill(
                                    Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                }
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

        topTarget.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                boolean success = false;
                System.out.println("dropped on the top");
                if (event.getGestureSource() != topTarget.getParent()) {

                    Task sourceTask = (Task)((Node)event.getGestureSource()).getUserData();
                    Task nextTask = (Task)(topTarget.getParent()).getUserData();
                    TaskList targetList = nextTask.getParentTaskList();

                    TaskCtrl.this.moveTaskTo(sourceTask, targetList, nextTask);
                    success = true;

                }
                event.setDropCompleted(success);
                event.consume();
            }
        });

        Pane bottomTarget = this.taskBottom;

        bottomTarget.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                boolean success = false;
                System.out.println("dropped on the bottom");
                if (event.getGestureSource() != bottomTarget.getParent()) {

                    Task sourceTask = (Task)((Node)event.getGestureSource()).getUserData();
                    Task prevTask = (Task)(bottomTarget.getParent()).getUserData();

                    Task nextTask = TaskCtrl.this.getNextTask(prevTask);
                    if (sourceTask.equals(nextTask)) {
                        event.setDropCompleted(false);
                        event.consume();
                        return;
                    }

                    TaskList targetList = prevTask.getParentTaskList();

                    TaskCtrl.this.moveTaskTo(sourceTask, targetList, nextTask);
                    success = true;

                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    /**
     * Set back the visual feedback of the object being dragged when done.
     */
    public void setDragDone() {
        BorderPane source = this.taskContainer;
        HBox content = this.contentContainer;
        source.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                System.out.println("drag done");
                source.setBackground(new Background(
                        new BackgroundFill(
                                Color.rgb(232, 213, 196), CornerRadii.EMPTY, Insets.EMPTY)));
                content.setBackground(new Background(
                        new BackgroundFill(
                                Color.rgb(232, 213, 196), CornerRadii.EMPTY, Insets.EMPTY)));
                event.consume();
            }
        });
    }

}
