package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class BoardCrtl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public BoardCrtl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void addTask() {
        mainCtrl.showAddTask();
    }

}
