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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;

public class MainCtrl {

    private Stage primaryStage;
    private BoardCtrl boardCtrl;
    private Scene board;
    private Board currentBoard;
    private AddTaskCtrl addTaskCtrl;
    private Scene addTask;
    private AddTaskListCtrl addTaskListCtrl;
    private Scene addTaskList;
    private List<TaskList> taskListList;
    private TaskListCtrl taskListCtrl;
    private Scene taskList;

    /**
     * Initializes the main controller, its stage, scenes, and associated controllers.
     * @param primaryStage the window for the app
     * @param boardCtrl pair that has board controller and its related scene
     * @param addTask pair that has addTask controller and its related scene
     * @param addTaskList pair that has ddTaskList controller and its related scene
     */
    public void initialize(Stage primaryStage, Pair<BoardCtrl, Parent> boardCtrl,
                           Pair<AddTaskListCtrl, Parent> addTaskList,
                           Pair<AddTaskCtrl, Parent> addTask) {
        this.primaryStage = primaryStage;

        this.boardCtrl = boardCtrl.getKey();
        this.board = new Scene(boardCtrl.getValue());

        this.addTaskListCtrl = addTaskList.getKey();
        this.addTaskList = new Scene(addTaskList.getValue());

        this.addTaskCtrl = addTask.getKey();
        this.addTask = new Scene(addTask.getValue());

        this.loadBoard();
        this.loadTaskLists();

        this.showBoard();
        this.primaryStage.show();
    }

    public void showBoard() {
        this.primaryStage.setTitle("Board");
        this.primaryStage.setScene(this.board);
//        boardCtrl.refresh();
    }

    public void showAddTask(TaskListCtrl taskListCtrl) {
        this.addTaskCtrl.setParentTaskListCtrl(taskListCtrl);
        this.primaryStage.setTitle("Add New Task");
        this.primaryStage.setScene(this.addTask);
//        addTask.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showAddTaskList() {
        this.primaryStage.setTitle("Add New Task List");
        this.primaryStage.setScene(this.addTaskList);
    }

    public void showTaskList() {
        this.primaryStage.setTitle("Task List");
        this.primaryStage.setScene(this.taskList);
    }

    public void loadBoard() {
        this.currentBoard = this.boardCtrl.getBoard();
    }

    public Board getCurrentBoard() {
        return this.currentBoard;
    }

    public void loadTaskLists() {
        this.taskListList = this.addTaskListCtrl.getTaskLists();
        for (TaskList t : this.taskListList) {
            this.boardCtrl.addTaskListToBoard(t);
            this.loadTasks(t);
        }
    }

    public void loadTasks(TaskList tasklist) {
        List<Task> tasks = this.addTaskCtrl.getTasks(tasklist);
//        for (Task t : tasks) {
//            this.taskListCtrl.addTaskToList(t.getName());
//        }
    }
}