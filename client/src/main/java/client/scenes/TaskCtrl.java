package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class TaskCtrl extends Node {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Task task;
    @FXML
    private Label taskTitle;

    @Inject
    public TaskCtrl(ServerUtils server, MainCtrl mainCtrl, String title) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.taskTitle = new Label(title);
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void edit() {
        this.mainCtrl.showEditTask(this.task);
    }

    /**
     * Deletes a task from the server and removes it from the task list.
     */
    public void delete() {
        try{
            this.server.deleteTask(this.task);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //might want to come up with a better way to do delete a task from the list
        // instead of just reloading the whole board.
        this.mainCtrl.loadTaskLists();
    }

    public void openTaskOverview() {
        System.out.println("Opening task overview for task " + this.taskTitle.getText());
    }

}
