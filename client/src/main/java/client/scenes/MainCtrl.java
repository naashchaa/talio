package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private AddTaskCtrl addTaskCtrl;
    private Scene addTask;

    private BoardCrtl boardCrtl;
    private Scene board;

    public void initialize(Stage primaryStage, Pair<BoardCrtl, Parent> board, Pair<AddTaskCtrl, Parent> addTask)  {
        this.primaryStage = primaryStage;
        this.boardCrtl = board.getKey();
        this.board = new Scene(board.getValue());

        this.addTask = new Scene(addTask.getValue());
        this.addTaskCtrl = addTask.getKey();

        primaryStage.setTitle("Task Overview");
        primaryStage.setScene(this.board);
        primaryStage.show();
    }

    public void showAddTask() {
        primaryStage.setTitle("Add New Task");
        primaryStage.setScene(addTask);
//        addTask.setOnKeyPressed(e -> addTask.keyPressed(e));
    }

}
