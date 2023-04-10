package client.services;

import client.scenes.BoardCtrl;
import client.scenes.TaskListCtrl;
import client.utils.ServerUtils;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class BoardService {

    private final ServerUtils server;

    @Inject
    public BoardService (ServerUtils server) {
        this.server = server;
    }


    public void loadContents(BoardCtrl ctrl) {
//        this.loadBoard(ctrl);
        this.loadTaskLists(ctrl);
    }

    /** This method loads the board object from the database.
     * @param ctrl Controller to load the board for
     */
//    public void loadBoard(BoardCtrl ctrl) {
//        try {
//            List<Board> boards = this.server.getBoard();
//            if (boards.size() == 0) {
//                Board newBoard = new Board("Default Board");
//                this.server.addBoard(newBoard);
//                ctrl.setBoard(this.server.getBoard().get(0));
//            }
//            ctrl.setBoard(boards.get(0));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void refreshBoardLater(BoardCtrl ctrl) {
        for (var list : ctrl.getListContainer().getChildren()) {
            TaskListCtrl listCtrl = (TaskListCtrl) list.getUserData();
            if (listCtrl != null)
                this.refreshTaskListLater(ctrl, listCtrl);
        }
    }

    public void refreshTaskListLater(BoardCtrl ctrl, TaskListCtrl list) {
        Platform.runLater(() -> this.refreshTaskList(ctrl, list));
    }

    /** This method refreshes the clientside task list object to display changes.
     * @param ctrl Parent controller to update in
     * @param list the list to update
     */
    public void refreshTaskList(BoardCtrl ctrl, TaskListCtrl list) {
        Optional<Node> listNode = ctrl
            .getListContainer() // get the HBox, which holds all the scene nodes
            .getChildren() // get the HBox's children -> task list nodes
            .stream() // turn it back into a stream
//            .filter(node -> (list.getTaskList().getId() == (node.getUserData() == null ? 0 :
//                (((TaskCtrl)node.getUserData()).getTask()).id))) // find the right task node
            .filter(node -> (list.getTaskList().getId() == (node.getUserData() == null ? 0 :
            (((TaskListCtrl)node.getUserData()).getTaskList().getId()))))
            .findFirst();
        if (listNode.isEmpty())
            throw new IllegalArgumentException("No node linked to the given task list was found");

        Label label = (Label) listNode.get().lookup("#taskListName");
        label.setText(list.getTaskList().getName()); // update node data
        listNode.get().setUserData(list);
    }

    /**
     * Method that refreshes all tasks in a task list.
     * @param ctrl
     * @param listCtrl
     */
    public void refreshTasksInTaskList(BoardCtrl ctrl, TaskListCtrl listCtrl) {
        Optional<Node> listNode = ctrl
                .getListContainer() // get the HBox, which holds all the scene nodes
                .getChildren() // get the HBox's children -> task list nodes
                .stream() // turn it back into a stream
                .filter(node -> (listCtrl.getTaskList().getId() == (node.getUserData() == null ? 0 :
                        (((TaskListCtrl)node.getUserData()).getTaskList().getId()))))
                .findFirst(); // has the task list node

        if (listNode.isEmpty())
            throw new IllegalArgumentException("No node linked to the given task list was found");

        Node taskListNode = listNode.get();
        ObservableList<Node> taskNodeList =
                ((TaskListCtrl)(taskListNode.getUserData())).getTaskContainer().getChildren();

        List<Task> a = this.server.getTasksByParentList(listCtrl.getTaskList());
    }

    public void loadTaskLists(BoardCtrl ctrl) {
        this.removeTaskLists(ctrl);
        List<TaskList> taskLists = this.server.getTaskListsByParentBoard(ctrl.getBoard());
        for (TaskList list : taskLists) {
            ctrl.addTaskListToBoard(list);
        }
    }

    /** This method removes all task lists in a board.
     * @param ctrl The board controller to remove for
     */
    public void removeTaskLists(BoardCtrl ctrl) {
        for (Node node : ctrl.getListContainer().getChildren()) {
            if (!"button_addtasklist".equals(node.getId()))
                ((TaskListCtrl) node.getUserData()).disconnect();
        }
        while (ctrl.getListContainer().getChildren().size() > 1) {
            ctrl.getListContainer().getChildren().remove(0);
        }
    }
}
