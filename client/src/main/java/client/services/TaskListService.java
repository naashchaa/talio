package client.services;

import client.scenes.BoardCtrl;
import client.scenes.TaskCtrl;
import client.scenes.TaskListCtrl;
import client.utils.ServerUtils;
import commons.Task;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskListService {
    private final ServerUtils server;

    @Inject
    public TaskListService(ServerUtils server) {
        this.server = server;
    }

    /**
     * Gets all tasks from the database and checks to see if they have to be
     * loaded in the task lists in the board.
     * @param ctrl the task list ctrl to load tasks into
     */
    public void loadTasks(TaskListCtrl ctrl) {
        this.removeTasks(ctrl);
        ctrl.disconnect();
        ctrl.connectToWebSockets();
        List<Task> tasks = this.server.getTasksByParentList(ctrl.getTaskList());
        for (Task t : tasks) {
            ctrl.addTaskToList(t);
        }
        this.sortTasks(ctrl);
    }

    /** This method removes all tasks in a list.
     * @param ctrl The list controller to remove for
     */
    public void removeTasks(TaskListCtrl ctrl) {
        List<Node> toRemove = new ArrayList<>();
        for (int i = 0; i < ctrl.getTaskContainer().getChildren().size(); i++) {
            if (!ctrl.getTaskContainer().getChildren().get(i).equals(ctrl.getHighlightDrop())) {
                toRemove.add(ctrl.getTaskContainer().getChildren().get(i));
                TaskCtrl ref = ((TaskCtrl)(ctrl.getTaskContainer().getChildren()
                        .get(i).getUserData()));
                ref.disconnect();
                ref = null;
            }
        }
        ctrl.getTaskContainer().getChildren().removeAll(toRemove);
    }

    /**
     * This method sorts tasks in a particular task list.
     * @param ctrl controller to sort for
     */
    public void sortTasks(TaskListCtrl ctrl) {
        if (ctrl.getTaskContainer().getChildren().size() < 2)
            return;

        int index = 0;
        ObservableList<Node> nodes = ctrl.getTaskContainer().getChildren();

        List<Task> tasks = nodes.stream()
            .map(node -> ((TaskCtrl)node.getUserData()).getTask())
            .collect(Collectors.toList());

        Optional<Task> finger = nodes.stream()
            .map(Node::getUserData)
            .map(task -> ((TaskCtrl)task).getTask())
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
     * @param ctrl asd
     * @param listCtrl fgg
     */
    public void deleteTaskList(BoardCtrl ctrl, TaskListCtrl listCtrl) {
        ObservableList<Node> listContainer = ctrl.getListContainer().getChildren();

        Optional<Node> nodeToRemove = listContainer.stream()
            .filter(node -> (node.getId() == null || !node.getId().equals("button_addtasklist")))
            .filter(node -> (((TaskListCtrl)node.getUserData()).getTaskList()).getId() ==
                listCtrl.getTaskList().getId())
            .findFirst();

        if (nodeToRemove.isEmpty())
            throw new IllegalArgumentException(
                "The provided task list is not referenced in any node");

        listContainer.remove(nodeToRemove.get());
    }
}
