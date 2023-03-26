package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.scene.Node;

public class TaskListCtrl extends Node{
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String name;

    @Inject
    public TaskListCtrl(ServerUtils server, MainCtrl mainCtrl,String name) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.name = name;
    }

    public void addTask() {
        this.mainCtrl.showAddTask();
    }
}
