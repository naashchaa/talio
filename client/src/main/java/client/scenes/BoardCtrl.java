package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class BoardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public BoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void addTask() {
        mainCtrl.showAddTask();
    }

    public void addTaskList() {
        mainCtrl.showAddTaskList();
    }
}
