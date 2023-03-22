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

public class MainCtrl {

    private Stage primaryStage;
    private BoardCtrl boardCtrl;
    private Scene board;
    private AddTaskCtrl addTaskCtrl;
    private Scene addTask;
    private AddTaskListCtrl addTaskListCtrl;
    private Scene addTaskList;

    /**
     * Initializes the main controller, its stage, scenes, and associated controllers.
     * @param primaryStage the window for the app
     * @param boardCtrl
     * @param addTaskList
     */
    public void initialize(Stage primaryStage, Pair<BoardCtrl, Parent> boardCtrl, Pair<AddTaskListCtrl, Parent> addTaskList, Pair<AddTaskCtrl, Parent> addTask) {
        this.primaryStage = primaryStage;

        this.boardCtrl = boardCtrl.getKey();
        this.board = new Scene(boardCtrl.getValue());

        this.addTaskListCtrl = addTaskList.getKey();
        this.addTaskList = new Scene(addTaskList.getValue());

        this.addTaskCtrl = addTask.getKey();
        this.addTask = new Scene(addTask.getValue());

        this.showBoard();
        this.primaryStage.show();
    }

    public void showBoard() {
        this.primaryStage.setTitle("Board");
        this.primaryStage.setScene(this.board);
//        boardCtrl.refresh();
    }

    public void showAddTask() {
        this.primaryStage.setTitle("Add New Task");
        this.primaryStage.setScene(this.addTask);
//        addTask.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showAddTaskList() {
        this.primaryStage.setTitle("Add New Task List");
        this.primaryStage.setScene(this.addTaskList);
    }

}