/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.Board;
import commons.Task;
import commons.TaskList;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.*;

public class MainCtrl {

    private Stage primaryStage;
    private Stage popup;

    private ConnectToServerCtrl connectToServerCtrl;
    private Scene connectToServer;

    private ApplicationOverviewCtrl appOverviewCtrl;
    private Scene appOverview;
    private BoardCtrl currentBoardCtrl;
    private Scene board;

    private Board currentBoard;
    private AddTaskCtrl addTaskCtrl;
    private Scene addTask;
    private AddTaskListCtrl addTaskListCtrl;
    private Scene addTaskList;
    private List<TaskList> taskListList;

    private EditTaskListCtrl editTaskListCtrl;
    private Scene editTaskList;

    private EditTaskCtrl editTaskCtrl;
    private Scene editTask;

    private DeleteTaskListCtrl deleteTaskListCtrl;
    private Scene deleteTaskList;
    private CreateBoardCtrl createBoardCtrl;
    private Scene createBoard;

    private AdminLoginCtrl adminLoginCtrl;
    private Scene adminLogin;

    private DeleteBoardCtrl deleteBoardCtrl;
    private Scene deleteBoard;

    private EditBoardCtrl editBoardCtrl;
    private Scene editBoard;

    private LeaveBoardCtrl leaveBoardCtrl;
    private Scene leaveBoard;

    private JoinBoardCtrl joinBoardCtrl;
    private Scene joinBoard;

    private JoinKeyCtrl joinKeyCtrl;
    private Scene joinKey;

    private TaskListCtrl taskListCtrl;
    private Scene taskList;
    private Map<Long, TaskListCtrl> taskListCtrls;
    private boolean isAdmin = false;

    /**
     * Initializes the main controller, its stage, scenes, and associated controllers.
     * @param primaryStage The window for the app.
     * @param scenes A list of pairs that have the controller and its related scene.
     */
    public void initialize(Stage primaryStage, List<Pair<?, Parent>> scenes) {
        this.primaryStage = primaryStage;
        this.popup = new Stage();

        this.connectToServerCtrl = (ConnectToServerCtrl) scenes.get(0).getKey();
        this.connectToServer = new Scene(scenes.get(0).getValue());

        this.appOverviewCtrl = (ApplicationOverviewCtrl) scenes.get(1).getKey();
        this.appOverview = new Scene(scenes.get(1).getValue());

        this.addTaskListCtrl = (AddTaskListCtrl) scenes.get(2).getKey();
        this.addTaskList = new Scene(scenes.get(2).getValue());

        this.addTaskCtrl = (AddTaskCtrl) scenes.get(3).getKey();
        this.addTask = new Scene(scenes.get(3).getValue());

        this.editTaskListCtrl = (EditTaskListCtrl) scenes.get(4).getKey();
        this.editTaskList = new Scene(scenes.get(4).getValue());

        this.editTaskCtrl = (EditTaskCtrl) scenes.get(5).getKey();
        this.editTask = new Scene(scenes.get(5).getValue());

        this.deleteTaskListCtrl = (DeleteTaskListCtrl) scenes.get(6).getKey();
        this.deleteTaskList = new Scene(scenes.get(6).getValue());

        this.createBoardCtrl = (CreateBoardCtrl) scenes.get(7).getKey();
        this.createBoard = new Scene(scenes.get(7).getValue());

        this.adminLoginCtrl = (AdminLoginCtrl) scenes.get(8).getKey();
        this.adminLogin = new Scene(scenes.get(8).getValue());

        this.deleteBoardCtrl = (DeleteBoardCtrl) scenes.get(9).getKey();
        this.deleteBoard = new Scene(scenes.get(9).getValue());

        this.editBoardCtrl = (EditBoardCtrl) scenes.get(10).getKey();
        this.editBoard = new Scene(scenes.get(10).getValue());

        this.leaveBoardCtrl = (LeaveBoardCtrl) scenes.get(11).getKey();
        this.leaveBoard = new Scene(scenes.get(11).getValue());

        this.joinBoardCtrl = (JoinBoardCtrl) scenes.get(12).getKey();
        this.joinBoard = new Scene(scenes.get(12).getValue());

        this.joinKeyCtrl = (JoinKeyCtrl) scenes.get(13).getKey();
        this.joinKey = new Scene(scenes.get(13).getValue());

        this.taskListList = new ArrayList<>();
        this.taskListCtrls = new HashMap<>();

        this.showConnectToServer();
        this.primaryStage.show();
    }

    public void setStageDimensions() {
        this.primaryStage.sizeToScene();
        this.primaryStage.setX(0);
        this.primaryStage.setY(0);
    }

    public void showConnectToServer() {
        this.primaryStage.setTitle("Connect to a server:");
        this.primaryStage.setScene(this.connectToServer);
    }

    public void showAppOverview() {
        this.primaryStage.setTitle("Talio");
        this.primaryStage.setScene(this.appOverview);
        this.loadBoards();
    }

    public void showCreateBoard() {
        this.popup.setTitle("Create Board");
        this.popup.setScene(this.createBoard);
        this.showPopUp();
    }

    public void showDeleteBoard(BoardCtrl boardctrl) {
        this.popup.setTitle("Delete Board");
        this.popup.setScene(this.deleteBoard);
        this.deleteBoardCtrl.setStuff(boardctrl, this.appOverviewCtrl);
        this.showPopUp();
    }

    public void showEditBoard(Board board) {
        this.popup.setTitle("Edit Board");
        this.popup.setScene(this.editBoard);
        this.editBoardCtrl.setStuff(board, this.appOverviewCtrl);
        this.showPopUp();
    }

    public void showRemoveBoard(BoardCtrl boardctrl){
        this.popup.setTitle("Remove Board");
        this.popup.setScene(this.leaveBoard);
        this.leaveBoardCtrl.setBoardCtrl(boardctrl);
        this.showPopUp();
    }

    public void showJoinBoard(){
        this.popup.setTitle("Join Board");
        this.popup.setScene(this.joinBoard);
        this.showPopUp();
    }

    public void showJoinKey(Board board){
        this.popup.setTitle("Join Key");
        this.popup.setScene(this.joinKey);
        this.joinKeyCtrl.setFields(board);
        this.showPopUp();
    }

    /** This brings up the add task popup.
     * @param taskListCtrl the parent task list controller
     */
    public void showAddTask(TaskListCtrl taskListCtrl) {
        this.addTaskCtrl.setParentTaskListCtrl(taskListCtrl);
        this.addTaskCtrl.connectToWebSockets();
        this.popup.setTitle("Add New Task");
        this.popup.setScene(this.addTask);
        this.showPopUp();
//        addTask.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showEditTask(Task task) {
        this.editTaskCtrl.connectToWebSockets();
        this.popup.setTitle("Edit Task");
        this.popup.setScene(this.editTask);
        this.editTaskCtrl.setTask(task);
        this.showPopUp();
    }

    public void showAddTaskList() {
        this.popup.setTitle("Add New Task List");
        this.popup.setScene(this.addTaskList);
        this.showPopUp();
    }

    public void showDeleteTaskList(TaskListCtrl taskListCtrl) {
        this.popup.setTitle("Delete Task List");
        this.popup.setScene(this.deleteTaskList);
        this.deleteTaskListCtrl.setTaskListCtrl(taskListCtrl);
        this.showPopUp();
    }

    public void showAdminLogin() {
        this.popup.setTitle("Admin Login");
        this.popup.setScene(this.adminLogin);
        this.showPopUp();
    }

    public void showPopUp() {
        this.popup.show();
    }

    public void hidePopUp() {
        this.popup.hide();
    }

    public void loadBoard() {
        this.currentBoard = this.currentBoardCtrl.getBoard();
    }

    public Board getCurrentBoard() {
        return this.currentBoard;
    }

    public BoardCtrl getCurrentBoardCtrl() {
        return this.currentBoardCtrl;
    }

    public void updateBoard(BoardCtrl boardController) {
        this.currentBoardCtrl = boardController;
        this.loadBoard();
    }

    public void loadBoards() {
        this.appOverviewCtrl.loadExistingBoards();
    }
    /**
     * The method that first removes tasks lists and then loads them again
     * from the server/database.
     */
    public void loadTaskLists() {
        Platform.runLater(this::loadTaskListsHelper);
    }

    /**
     * This helped is made so that loading task lists with long polling has
     * no JAVAFX thread errors.
     */
    public void loadTaskListsHelper() {
        this.taskListList = this.addTaskListCtrl.getTaskLists();
        this.currentBoardCtrl.removeTaskLists();
        this.safelyRemoveTaskListCtrls();
        for (TaskListCtrl tlc : this.taskListCtrls.values()) {
            tlc.connectToWebSockets();
            tlc.sortTasks();
        }
        for (TaskList tl : this.taskListList) {
            this.taskListCtrls.put(tl.getId(), this.currentBoardCtrl.addTaskListToBoard(tl));
            this.loadTasks(tl);
        }

    }

    /**
     * This safely disconnect the STOMP session from a task list controller.
     */
    public void safelyRemoveTaskListCtrls() {
        for (TaskListCtrl tlc : this.taskListCtrls.values()) {
            tlc.disconnectStompSession();
        }
        this.taskListCtrls.clear();
    }
    /**
     * Gets all tasks from the database and checks to see if they have to be
     * loaded in the task lists in the board.
     * @param tasklist the task list from where the tasks are from
     */
    public void loadTasks(TaskList tasklist) {
        TaskListCtrl ctrl = this.taskListCtrls.get(tasklist.getId());
        if (ctrl == null) {
            throw new IllegalArgumentException("Controller doesnt exist");
        }
        ctrl.removeTasks();
        List<Task> tasks = this.addTaskCtrl.getTasks(tasklist);
        for (Task t : tasks) {
            if (t.getParentTaskList().equals(ctrl.getTaskList())) {
                ctrl.addTaskToList(t);
            }
        }
        ctrl.sortTasks();
    }



    public void refreshTaskLater(Task task) {
        Platform.runLater(() -> {
            this.refreshTask(task);
        });
    }

    /** This abomination exists to confuse everyone (and also to refresh the displayed task).
     * @param task the updated task object
     */
    public void refreshTask(Task task) {
        Optional<TaskListCtrl> parentTaskListCtrl = this.taskListCtrls
            .values() // get all task list controllers
            .stream() // turn them into a stream for functional programming
            .filter(ctrl -> ctrl.getTaskList().equals(task.getParentTaskList())) // filter
            .findFirst();

        if (parentTaskListCtrl.isEmpty())
            throw new IllegalArgumentException(
                "The provided task does not belong to any task list");

        Optional<Node> taskNode = parentTaskListCtrl
            .get() // find the parent task list controller of the given task
            .getTaskContainer() // get the VBox, which holds all the scene nodes
            .getChildren() // get the VBox's children
            .stream() // turn it back into a stream
            .filter(node -> (task.id == (node.getUserData() == null ? 0 :
                ((Task)node.getUserData()).id))) // find the right task node
            .findFirst();

        if (taskNode.isEmpty())
            throw new IllegalArgumentException("No node linked to the given task was found");

        Label label = (Label) taskNode.get().lookup("#taskTitle");
        label.setText(task.getName()); // update note data
        taskNode.get().setUserData(task);
    }

    public void deleteTaskLater(Task task) {
        Platform.runLater(() -> {
            this.deleteTask(task);
        });
    }



    /** This lovely piece of functional programming removes the frontend task node.
     * @param task The associated task that should be deleted
     * @return returns the frontend node object of the deleted task
     */
    public Node deleteTask(Task task) {
        Optional<TaskListCtrl> parentTaskListCtrl = this.taskListCtrls.values()
            .stream()
            .filter(ctrl -> ctrl.getTaskList().equals(task.getParentTaskList()))
            .findFirst();

        if (parentTaskListCtrl.isEmpty())
            throw new IllegalArgumentException("The provided task is not part of any task list");

        ObservableList<Node> taskContainer = parentTaskListCtrl
            .get()
            .getTaskContainer()
            .getChildren();

        Optional<Node> nodeToRemove = taskContainer.stream()
            .filter(node -> ((Task)node.getUserData()).id == task.id)
            .findFirst();

        if (nodeToRemove.isEmpty())
            throw new IllegalArgumentException("The provided task is not referenced in any node");

        taskContainer.remove(nodeToRemove.get());

        parentTaskListCtrl.get().addDummyPane();

        return nodeToRemove.get();
    }

    /**
     * This method adds a given task to a given list.
     * @param taskList list to add to
     * @param task task to add
     */
    public void addTaskToList(TaskList taskList, Task task) {
        TaskListCtrl listCtrl = this.taskListCtrls.get(taskList.getId());
        if (listCtrl == null) {
            return;
        }
        listCtrl.addTaskToList(task);
    }

    public void showEditTaskList(TaskList taskList) {
        this.popup.setTitle("Edit Task List");
        this.popup.setScene(this.editTaskList);
        this.editTaskListCtrl.setTaskList(taskList);
        this.showPopUp();
    }

    public void deleteTaskList(TaskListCtrl ctrl) {
        this.taskListList.remove(ctrl.getTaskList());
        this.taskListCtrls.remove(ctrl.getTaskList().getId());
        // should be replaced with method that removes the last task list from the hbox
        // of board ctrl -> loadTaskLists() is slow.
        this.loadTaskLists();
    }

    public Map<Long, TaskListCtrl> getTaskListCtrls() {
        return this.taskListCtrls;
    }

    public void addTaskList(TaskList taskList) {
        this.taskListList.add(taskList);
    }

    public void setAdmin(boolean b) {
        this.isAdmin = b;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }
}