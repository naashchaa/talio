package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskListCtrl extends Node implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private TaskList taskList;
    private ObservableList<Task> data;
    @FXML
    private VBox taskContainer;
    @FXML
    private Label taskListName;

    @Inject
    public TaskListCtrl(ServerUtils server, MainCtrl mainCtrl, String name) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // The task list ctrl gets subscribed to the following path.
        // Now it can receive updates that are sent back from the server to this path.
        this.server.registerForMessages("/topic/tasks/add", Task.class, task -> {
            // makes sure that the parent task list is the only that shows task on client.
            if (this.taskList.equals(task.getParentTaskList())) {
                // this method is used to call runLater() to avoid JAVAFX thread errors.
                this.loadTasksLater(task);
            }
        });
    }

    /**
     * Helper method to be able to use runLater().
     * @param task The task to add to a task list.
     */
    public void loadTasksLater(Task task) {
        Platform.runLater(() -> {
            this.loadTasksLaterHelper(task);
        });
    }

    /**
     * Send a parent task list and a task to be added to that task list
     * in the main ctrl.
     * @param task the task to be added.
     */
    public void loadTasksLaterHelper(Task task) {
        //this.mainCtrl.loadTasksOfTaskList(this.taskList);
        this.mainCtrl.addTaskToList(task.getParentTaskList(), task);
    }

    public void addTask() {
        this.mainCtrl.showAddTask(this);
    }

    /**
     * Creates a JAVAFX scene and controller for a task and gives it
     * the assigned name, then adds it to the children of the container
     * that has all the tasks of the list.
     * @param name the name to give the task.
     */
    public void addTaskToList(String name) {
        Pair<TaskCtrl, Parent> pair =
                Main.FXML.load(TaskCtrl.class, "client", "scenes", "Task.fxml");
        Label taskName = (Label) pair.getValue().lookup("#taskTitle");
        taskName.setText(name);
        this.taskContainer.getChildren().add(pair.getValue());
    }

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    public TaskList getTaskList() {
        return this.taskList;
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
        this.server.deleteTaskList(this.taskList);
        this.mainCtrl.deleteTaskList(this);
    }

    public void setTaskListName(String newName) {
        this.taskListName.setText(newName);
    }

    /**
     * Deletes all tasks inside the task lists, only visually.
     * They are still stored on the database.
     */
    public void deleteTasks() {
        this.taskContainer.getChildren().clear();
    }

    public void disconnectStompSession() {
        this.server.disconnectStompSession();
    }
}
