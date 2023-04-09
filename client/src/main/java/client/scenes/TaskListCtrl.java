package client.scenes;

import client.Main;
import client.services.TaskService;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.input.DragEvent;
import javafx.util.Pair;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class TaskListCtrl extends Node implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final TaskService service;
    private TaskList taskList;
    @FXML
    private VBox taskContainer;
    @FXML
    private Label taskListName;
    @FXML
    private Pane highlightDrop;

    @Inject
    public TaskListCtrl(ServerUtils server, MainCtrl mainCtrl, TaskService service) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.service = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.connectToWebSockets();
        this.service.connectToWebSockets();
        // The task list ctrl gets subscribed to the following path.
        // Now it can receive updates that are sent back from the server to this path.
        this.server.registerForMessages("/topic/tasks/add", Task.class, task -> {
            // makes sure that the parent task list is the only that shows task on client.
            if (this.taskList.getId().equals(task.getParentTaskList().getId())) {
                // this method is used to call runLater() to avoid JAVAFX thread errors.
                // this.loadTasksLater(); // this is causing an error because of remove method
                this.showLastAddedTask(task);
            }
        });
        this.server.registerForMessages("/topic/taskList/edit", TaskList.class, taskList -> {
            if (this.taskList.getId().equals(taskList.getId())) {
                this.updateTaskListName(taskList);
            }
        });
        this.server.registerForMessages("/topic/taskList/delete", TaskList.class, taskList -> {
            if (this.taskList.getId().equals(taskList.getId())) {
                this.removeThisCtrl(taskList);
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
        this.setDragMethods();
    }

    public void showLastAddedTask(Task task) {
        Platform.runLater(() -> {
            this.addTaskToList(task);
        });
    }

    /**
     * When a task list is deleted, its associated controller should also be deleted.
     * It also should disappear from the children of the board.
     * @param taskList the task list removed.
     */
    public void removeThisCtrl(TaskList taskList) {
        Platform.runLater(() -> {
            this.mainCtrl.removeChildFromHBox(taskList);
            //this.mainCtrl.deleteTaskList(this);
        });
    }

    public void updateTaskListName(TaskList taskList) {
        Platform.runLater(() -> {
            this.taskListName.setText(taskList.getName());
        });
    }

    /**
     * This method sorts tasks in a particular task list.
     */
    public void sortTasks() {
        if (this.taskContainer.getChildren().size() < 2)
            return;

        int index = 0;
        ObservableList<Node> nodes = this.taskContainer.getChildren();
        List<Task> tasks = nodes.stream()
            .map(node -> (Task)node.getUserData())
            .collect(Collectors.toList());

        Optional<Task> finger = nodes.stream()
            .map(Node::getUserData)
            .map(task -> (Task)task)
            .filter(task -> task.getPrevTask() == 0)
            .findFirst();

        if (finger.isEmpty())
            throw new IllegalArgumentException("Unable to find a finger with an empty prev task");

        while (index < nodes.size() - 1) {
            Task finalFinger = finger.get();
            Optional<Task> nextTask = tasks.stream()
                .filter(task -> task.getPrevTask() == finalFinger.id)
                .findFirst();

            if (nextTask.isEmpty())
                throw new IllegalArgumentException("Unable to find a task next to finger");

            int taskIndex = tasks.indexOf(nextTask.get());
            nodes.add(nodes.remove(taskIndex));
            tasks.add(tasks.remove(taskIndex));
            index++;
            finger = nextTask;
        }
    }

    /**
     * Helper method to be able to use runLater().
     */
    public void loadTasksLater() {
        Platform.runLater(() -> {
            this.loadTasksLaterHelper();
        });
    }

    /**
     * Send a parent task list and a task to be added to that task list
     * in the main ctrl.
     */
    public void loadTasksLaterHelper() {
        this.removeTasks();
        for(Task task : this.server.getTasksOfTasklist(this.taskList)) {
            this.addTaskToList(task);
        }
        this.sortTasks();
    }

    public void addTask() {
        this.mainCtrl.showAddTask(this);
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
        pair.getValue().setUserData(task);
        pair.getKey().setTask(task);
        this.taskContainer.getChildren().add(pair.getValue());
    }

    public void removeTask() {
        while (this.taskContainer.getChildren().size() > 1) {
            this.taskContainer.getChildren().remove(0);
        }
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

    public void edit() {
        this.mainCtrl.showEditTaskList(this.taskList);
    }

    /**
     * This method first deletes all tasks in a list
     * and then proceeds to delete the list.
     */
    public void delete() {
        this.server.deleteTasksParentTaskList(this.taskList);
        this.server.send("/app/taskList/delete", this.taskList);
//        this.server.deleteTaskList(this.taskList);
    }

    public void showDeleteTaskList() {
        this.mainCtrl.showDeleteTaskList(this);
    }

    public void setTaskListName(String newName) {
        this.taskListName.setText(newName);
    }

    public void disconnectStompSession() {
        this.server.disconnectStompSession();
    }
    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }

    /**
     * Removes all the nodes in a task list.
     */
    public void removeTasks() {
        List<Node> toRemove = new ArrayList<>();
        for (int i = 0; i < this.taskContainer.getChildren().size(); i++) {
            if (!this.taskContainer.getChildren().get(i).equals(this.highlightDrop)) {
                toRemove.add(this.taskContainer.getChildren().get(i));
            }
        }
        this.taskContainer.getChildren().removeAll(toRemove);
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
        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();
            }
        });
    }

    /**
     * Sets the logic for when a drag and drop entity passes over this task list.
     */
    public void setDragEntered() {
        VBox target = this.taskContainer;
        target.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (target.getChildren().size() == 1 &&
                    target.getChildren().get(0).equals(TaskListCtrl.this.getHighlightDrop()))
                    target.getChildren().get(0).setVisible(true);
            }
        });
    }

    /**
     * Sets the logic for when a drag and drop exits this task list.
     */
    public void setDragExited() {
        VBox target = this.taskContainer;
        target.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (target.getChildren().size() == 1 &&
                    target.getChildren().get(0).equals(TaskListCtrl.this.getHighlightDrop()))
                    target.getChildren().get(0).setVisible(false);
            }
        });
    }

    /**
     * Sets the logic for when a drag and drop is released over this task list.
     */
    public void setDragDropped() {
        VBox target = this.taskContainer;
        TaskListCtrl ctrl = this;

        target.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Task sourceTask = (Task)((Node)event.getGestureSource()).getUserData();
                TaskList targetList = TaskListCtrl.this.getTaskList();
                Long prevId = sourceTask.getParentTaskList().getId();
                TaskListCtrl.this.service.moveTaskTo(sourceTask, targetList, null);
                List<Long> ids = new ArrayList<>();
                ids.add(prevId);
                ids.add(targetList.getId());
                TaskListCtrl.this.server.send("/app/tasks/drag", ids);
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    public VBox getContainer() {
        return this.taskContainer;
    }

    public Pane getHighlightDrop() {
        return this.highlightDrop;
    }
}
