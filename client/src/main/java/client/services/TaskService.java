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

    /** This method returns a task controller based on the associated task id.
     * @param caller the caller controller
     * @param id the id of the task
     * @return the associated controller
     */
    public Optional<TaskCtrl> getCtrlByTaskId(TaskCtrl caller, long id) {
        BoardCtrl board = caller.getParentCtrl().getParentCtrl();

        return board.getListContainer()
            .getChildren()
            .stream()
            .map(Node::getUserData)
            .map(obj -> (TaskListCtrl)obj)
            .map(TaskListCtrl::getTaskContainer)
            .map(VBox::getChildren)
            .map(ObservableList::stream)
            .map(stream ->
                stream.filter(node ->
                        (id == (node.getUserData() == null ? 0 :
                                (((TaskCtrl)node.getUserData()).getTask().id))
                        )
                    )
                    .map(Node::getUserData)
                    .map(obj -> (TaskCtrl)obj)
                    .findFirst()
            )
            .filter(Optional::isPresent)
            .map(Optional::get)
            .findFirst();
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

    public Optional<TaskCtrl> getNextByTaskId(TaskCtrl caller, long id) {
        Optional<TaskCtrl> nextTask = Optional.empty();
        if (id != 0)
            nextTask = this.getCtrlByTaskId(caller, id);

        return nextTask;
    }

    /** This method returns the task list controller associated to a certain task list.
     * @param caller the caller controller
     * @param id the task list id
     * @return the task list controller
     */
    public Optional<TaskListCtrl> getCtrlByTaskListId(TaskCtrl caller, long id) {
        //get parent board
        BoardCtrl board = caller.getParentCtrl().getParentCtrl();

        return board.getListContainer()
            .getChildren()
            .stream()
            .map(Node::getUserData)
            .map(obj -> (TaskListCtrl)obj)
            .filter(ctrl -> id == ctrl.getTaskList().getId())
            .findFirst();
    }

    public boolean nodeInList(Node node, TaskListCtrl ctrl) {
        return ctrl.getTaskContainer().getChildren().contains(node);
    }

    /** This method finds the drag and drop parameters based on a list of ids.
     * @param caller the caller task controller
     * @param ids the list of needed ids
     * @return a list with all the parameters
     */
    public List<Object> getDragParameters(TaskCtrl caller, List<?> ids) {
        List<Object> list = new ArrayList<>();

        //find source task controller
        Optional<TaskCtrl> sourceTaskCtrl =
            this.getCtrlByTaskId(caller, ((Long) ids.get(0)));

        if (sourceTaskCtrl.isEmpty())
            throw new IllegalArgumentException("Could not find the source task controller");

        //find target list controller
        Optional<TaskListCtrl> targetListCtrl =
            this.getCtrlByTaskListId(caller, ((Long) ids.get(1)));

        if(targetListCtrl.isEmpty())
            throw new IllegalArgumentException("Could not find the target list controller");

        //find next task
        Optional<TaskCtrl> nextTaskCtrl =
            this.getNextByTaskId(caller, ((Long) ids.get(2)));

        Task nextTask = null;
        if (nextTaskCtrl.isPresent())
            nextTask = nextTaskCtrl.get().getTask();

        list.add(sourceTaskCtrl.get());
        list.add(targetListCtrl.get());
        list.add(nextTask);

        return list;
    }

    /** This checks if a certain task has already been dragged to hopefully not get updated twice.
     * @param ctrl the controller of the task to check
     * @param ids the list of ids of drag and drop parameters
     * @return true if it has already been dragged, false otherwise
     */
    public boolean isAlreadyDragged(TaskCtrl ctrl, List<?> ids) {
        // check if the correlated node element is in its parent controller's node children
        Optional<Node> taskNode = this.getNodeByTaskId(ctrl, ((Long) ids.get(1)));

        if (taskNode.isEmpty())
            throw new IllegalArgumentException("Could not find the node by ID");

        Optional<TaskListCtrl> newParentCtrl = this.getCtrlByTaskListId(ctrl, ((Long) ids.get(1)));

        if (newParentCtrl.isEmpty())
            throw new IllegalArgumentException("Could not find the list by ID");

        if (!this.nodeInList(taskNode.get(), newParentCtrl.get()))
            return false;

        // check if the controller's next task matches that of the drag and drop destination
        Task ctrlNext = ctrl.getNextTask(ctrl.getTask()).getTask();
        Optional<TaskCtrl> taskNextCtrl = this.getNextByTaskId(ctrl, (Long) ids.get(2));
        Task taskNext = taskNextCtrl.isEmpty() ? null : taskNextCtrl.get().getTask();
        long id1 = ctrlNext == null ? 0 : ctrlNext.id;
        long id2 = taskNext == null ? 0 : taskNext.id;

        return id1 == id2;
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

//        if (!sourceTaskCtrl.getParentCtrl().equals(targetListCtrl))
//            sourceTaskCtrl.setParentCtrl(targetListCtrl);
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
