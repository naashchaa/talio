package client.services;

import client.scenes.MainCtrl;
import client.scenes.TaskListCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import java.util.Optional;

public class TaskService {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public TaskService(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }

    /** This method returns the task that comes after or below a certain task.
     * @param task the task to look ahead of
     * @return the task after the task parameter
     */
    public Task getNextTask(Task task) {
        Optional<TaskListCtrl> parentTaskListCtrl = this.mainCtrl.getTaskListCtrls()
            .values()
            .stream()
            .filter(ctrl -> ctrl.getTaskList().equals(task.getParentTaskList()))
            .findFirst();

        if (parentTaskListCtrl.isEmpty())
            return null;

        Optional<Node> nextNode = parentTaskListCtrl.get()
            .getTaskContainer()
            .getChildren()
            .stream()
            .filter(node -> (task.id == (node.getUserData() == null ? 0 :
                ((Task)node.getUserData()).getPrevTask())))
            .findFirst();

        return (Task)nextNode.map(Node::getUserData).orElse(null);
    }

    /** This method moves one task from another with the ability to move between lists.
     * @param taskToMove task to move
     * @param listToMoveTo list to move into
     * @param nextTask the task that's next or below the moved task's new position
     */
    public void moveTaskTo(Task taskToMove, TaskList listToMoveTo, Task nextTask) {
        // get the old node
        Node taskNode = this.mainCtrl.deleteTask(taskToMove);

        // get the list of nodes in the list to insert into
        Optional<TaskListCtrl> parentTaskListCtrl = this.mainCtrl.getTaskListCtrls().values()
            .stream()
            .filter(ctrl -> ctrl.getTaskList().equals(listToMoveTo))
            .findFirst();

        if (parentTaskListCtrl.isEmpty())
            throw new IllegalArgumentException(
                "The supplied task list does not belong in any controller");

        ObservableList<Node> nodeList = parentTaskListCtrl
            .get()
            .getTaskContainer()
            .getChildren();

        // get the insert index
        int insertIndex = nodeList.size();

        // find the old next task and update the prev
        Task oldNextTask = this.getNextTask(taskToMove);
        if (oldNextTask != null) {
            oldNextTask.setPrevTask(taskToMove.getPrevTask());
            this.server.send("/app/tasks/edit", oldNextTask);
            this.mainCtrl.refreshTask(oldNextTask);
        }

        long prevTask;  // a real task or null

        taskToMove.setParentTaskList(listToMoveTo); // move to the right task list

        // edge case: next is null -> task goes at the end of list
        if (nextTask != null) {
            prevTask = nextTask.getPrevTask();
            nextTask.setPrevTask(taskToMove.id);
            Optional<Node> nextTaskNode = nodeList.stream()
                .filter(node -> (node.getUserData()).equals(nextTask))
                .findFirst();

            if (nextTaskNode.isEmpty())
                throw new IllegalArgumentException(
                    "Next task does not belong to the specified task list");

            insertIndex = nodeList.indexOf(nextTaskNode.get());
        }
        else {
            prevTask = (nodeList.size() == 1 &&
                nodeList.get(0).equals(parentTaskListCtrl.get().getHighlightDrop())) ?
                0 : ((Task)nodeList.get(nodeList.size() - 1).getUserData()).id;
        }

        // set the prev of the insert to the right prev
        taskToMove.setPrevTask(prevTask);

        // potentially remove dummy pane
        int offset = parentTaskListCtrl.get().removeDummyPane() ? 1 : 0;

        // insert the old node into new location
        nodeList.add(insertIndex - offset, taskNode);

        // update the next and inserted tasks
        this.server.send("/app/tasks/edit", nextTask);
        this.server.send("/app/tasks/edit", taskToMove);

    }



}
