package client.services;

import client.scenes.BoardCtrl;
import client.scenes.TaskCtrl;
import client.scenes.TaskListCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.*;

public class TaskService {
    private final ServerUtils server;

    @Inject
    public TaskService(ServerUtils server) {
        this.server = server;
    }

    public void connectToWebSockets() {
        this.server.terminateWebSocketConnection();
        this.server.establishWebSocketConnection();
    }

    /** This method returns the task that comes after or below a certain task.
     * @param ctrl the parent controller to look in
     * @param task the task to look ahead of
     * @return the task after the task parameter
     */
    public TaskCtrl getNextTask(TaskListCtrl ctrl, Task task) {
        Optional<Node> nextNode = ctrl
            .getTaskContainer()
            .getChildren()
            .stream()
            .filter(node -> (task.id == (node.getUserData() == null ? 0 :
                (((TaskCtrl)node.getUserData()).getTask()).getPrevTask())))
            .findFirst();

        return (TaskCtrl)nextNode.map(Node::getUserData).orElse(null);
    }

    /** This method returns the node associated with a certain task.
     * @param caller the caller controller
     * @param id the task id
     * @return the task node
     */
    public Optional<Node> getNodeByTaskId(TaskCtrl caller, long id) {
        BoardCtrl board = caller.getParentCtrl().getParentCtrl();

        var streams = board.getListContainer()
            .getChildren()
            .stream()
            .map(Node::getUserData)
            .filter(Objects::nonNull)
            .map(obj -> (TaskListCtrl)obj)
            .map(TaskListCtrl::getTaskContainer)
            .map(VBox::getChildren)
            .map(ObservableList::stream);

        return streams
            .map(stream ->
                stream.filter(node -> node.getUserData() != null)
                    .filter(node ->
                        ((TaskCtrl) node.getUserData()).getTask().id == id)
                    .findFirst()
            )
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();
    }

    /** This method moves one task from another with the ability to move between lists.
     * @param sourceTaskCtrl task controller corresponding to the task to move
     * @param targetListCtrl list controller to move into
     * @param nextTask the task that's next or below the moved task's new position
     */
    public void moveTaskTo(TaskCtrl sourceTaskCtrl,
                           TaskListCtrl targetListCtrl, Task nextTask) {
        Task taskToMove = sourceTaskCtrl.getTask();
        TaskList listToMoveTo = targetListCtrl.getTaskList();
        // get the old node
        Node taskNode = this.getTaskNode(sourceTaskCtrl.getParentCtrl(), taskToMove);
        this.deleteTask(sourceTaskCtrl.getParentCtrl(), taskToMove);

        ObservableList<Node> nodeList = targetListCtrl
            .getTaskContainer()
            .getChildren();

        // get the insert index
        int insertIndex = nodeList.size();

        // find the old next task and update the prev
        TaskCtrl oldNextTaskCtrl = this.getNextTask(sourceTaskCtrl.getParentCtrl(), taskToMove);
        if (oldNextTaskCtrl != null) {
            oldNextTaskCtrl.getTask().setPrevTask(taskToMove.getPrevTask());
            this.server.send("/app/tasks/edit", oldNextTaskCtrl.getTask());
            this.refreshTask(oldNextTaskCtrl);
        }

        long prevTask;  // a real task or null

        taskToMove.setParentTaskList(listToMoveTo); // move to the right task list

        // edge case: next is null -> task goes at the end of list
        if (nextTask != null) {
            prevTask = nextTask.getPrevTask();
            nextTask.setPrevTask(taskToMove.id);
            Optional<Node> nextTaskNode = nodeList.stream()
                .filter(node -> (((TaskCtrl)node.getUserData()).getTask()).equals(nextTask))
                .findFirst();

            if (nextTaskNode.isEmpty())
                throw new IllegalArgumentException();

            insertIndex = nodeList.indexOf(nextTaskNode.get());
        }
        else {
            prevTask = (nodeList.size() == 1 &&
                nodeList.get(0).equals(targetListCtrl.getHighlightDrop())) ?
                0 : (((TaskCtrl)nodeList.get(nodeList.size() - 1).getUserData()).getTask()).id;
        }

        // set the prev of the insert to the right prev
        taskToMove.setPrevTask(prevTask);

        // potentially remove dummy pane
        int offset = targetListCtrl.removeDummyPane() ? 1 : 0;

        // insert the old node into new location
        nodeList.add(insertIndex - offset, taskNode);

        sourceTaskCtrl.setParentCtrl(targetListCtrl);

        // update the next and inserted tasks
        if (nextTask != null)
            this.server.send("/app/tasks/edit", nextTask);
        this.server.send("/app/tasks/edit", taskToMove);
    }

    public void refreshTaskLater(TaskCtrl task) {
        Platform.runLater(() -> this.refreshTask(task));
    }

    /** This abomination exists to confuse everyone (and also to refresh the displayed task).
     * @param task the updated task object
     */
    public void refreshTask(TaskCtrl task) {
        Optional<Node> taskNode = this.getNodeByTaskId(task, task.getTask().id);

        if (taskNode.isEmpty())
            throw new IllegalArgumentException("No node linked to the given task was found");

        Label label = (Label) taskNode.get().lookup("#taskTitle");
        label.setText(task.getTask().getName()); // update node data
        taskNode.get().setUserData(task);
    }

    /** This method returns the node associated with a certain task.
     * @param ctrl the parent controller
     * @param task the task to find
     * @return the task node
     */
    public Node getTaskNode(TaskListCtrl ctrl, Task task) {
        Optional<Node> returnNode = ctrl
            .getTaskContainer()
            .getChildren()
            .stream()
            .filter(node -> (((TaskCtrl)node.getUserData()).getTask()).id == task.id)
            .findFirst();

        if (returnNode.isEmpty())
            throw new IllegalArgumentException("The provided task is not referenced in any node");

        return returnNode.get();
    }

    public void deleteTaskLater(TaskListCtrl ctrl, Task task) {
        Platform.runLater(() -> this.deleteTask(ctrl, task));
    }

    /**
     * This lovely piece of functional programming removes the frontend task node.
     * @param ctrl The parent controller
     * @param task The associated task that should be deleted
     */
    public void deleteTask(TaskListCtrl ctrl, Task task) {
        ObservableList<Node> taskContainer = ctrl
            .getTaskContainer()
            .getChildren();

        Optional<Node> nodeToRemove = taskContainer.stream()
            .filter(node -> (((TaskCtrl)node.getUserData()).getTask()).id == task.id)
            .findFirst();

        if (nodeToRemove.isEmpty()) {
            return;
            //throw new IllegalArgumentException("The provided task is not referenced in any node");
        }

        taskContainer.remove(nodeToRemove.get());

        ctrl.addDummyPane();
    }

}
