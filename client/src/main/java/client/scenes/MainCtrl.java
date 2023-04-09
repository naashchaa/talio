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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.List;

public class MainCtrl {

    private Stage primaryStage;
//    private ConnectToServerCtrl connectToServerCtrl;
    private Stage popup;

    private ConnectToServerCtrl connectToServerCtrl;
    private Scene connectToServer;

    private ApplicationOverviewCtrl appOverviewCtrl;
    private Scene appOverview;
    private BoardCtrl currentBoardCtrl;
    private Scene board;
    private AddTaskCtrl addTaskCtrl;
    private Scene addTask;
    private AddTaskListCtrl addTaskListCtrl;
    private Scene addTaskList;
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
    private boolean isAdmin = false;

    /**
     * Initializes the main controller, its stage, scenes, and associated controllers.
     * @param primaryStage The window for the app.
     * @param scenes A list of pairs that have the controller and its related scene.
     */
    public void initialize(Stage primaryStage, List<Pair<?, Parent>> scenes) {
        this.primaryStage = primaryStage;
        this.popup = new Stage();

//        this.connectToServerCtrl = (ConnectToServerCtrl) scenes.get(0).getKey();
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

    public void showBoard() {
        this.currentBoardCtrl.loadContents();
        this.primaryStage.setTitle("Board");
        this.primaryStage.setScene(this.board);
        this.setStageDimensions();
    }

    public void showAppOverview() {
        this.primaryStage.setTitle("Talio");
        this.primaryStage.setScene(this.appOverview);
        this.appOverviewCtrl.loadExistingBoards();
    }

    public void showCreateBoard() {
        this.popup.setTitle("Create Board");
        this.popup.setScene(this.createBoard);
        this.showPopUp();
    }

    public void showEditBoard(BoardCtrl ctrl) {
        this.editBoardCtrl.setParentCtrl(ctrl);
        this.popup.setTitle("Edit Board");
        this.popup.setScene(this.editBoard);
        this.showPopUp();
    }

    public void showDeleteBoard(BoardCtrl boardctrl) {
        this.popup.setTitle("Delete Board");
        this.popup.setScene(this.deleteBoard);
        this.deleteBoardCtrl.setStuff(boardctrl, this.appOverviewCtrl);
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

    public void showJoinKey(BoardCtrl ctrl){
        this.joinKeyCtrl.setFields(ctrl);
        this.popup.setTitle("Join Key");
        this.popup.setScene(this.joinKey);
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
    }

    public void showEditTask(TaskCtrl ctrl) {
        this.editTaskCtrl.connectToWebSockets();
        this.editTaskCtrl.setTaskCtrl(ctrl);
        this.popup.setTitle("Edit Task");
        this.popup.setScene(this.editTask);
        this.showPopUp();
    }

    public void showAddTaskList() {
        this.addTaskListCtrl.setParentBoardCtrl(this.currentBoardCtrl);
        this.addTaskListCtrl.connectToWebSockets();
        this.popup.setTitle("Add New Task List");
        this.popup.setScene(this.addTaskList);
        this.showPopUp();
    }

    public void showEditTaskList(TaskListCtrl ctrl) {
        this.editTaskListCtrl.setTaskListCtrl(ctrl);
        this.editTaskListCtrl.connectToWebSockets();
        this.popup.setTitle("Edit Task List");
        this.popup.setScene(this.editTaskList);
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

    public BoardCtrl getCurrentBoardCtrl() {
        return this.currentBoardCtrl;
    }

    public void updateBoard(BoardCtrl boardController) {
        this.currentBoardCtrl = boardController;
    }

    public void loadBoards() {
        this.appOverviewCtrl.loadExistingBoards();
    }

    public void setAdmin(boolean b) {
        this.isAdmin = b;

    }

    public boolean isAdmin() {
        return this.isAdmin;
    }
}