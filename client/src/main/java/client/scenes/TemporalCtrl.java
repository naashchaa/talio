package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class TemporalCtrl {

    private Stage primaryStage;
    private AddTaskCtrl addtaskCtrl;
    private Scene overview;
    private AddTaskListCtrl addTaskList;

    public void initialize(Stage primaryStage, Pair<AddTaskListCtrl, Parent> addTaskList) {
        this.primaryStage = primaryStage;
        this.addTaskList = addTaskList.getKey();
        this.overview = new Scene(addTaskList.getValue());
        primaryStage.setTitle("Task Overview");
        primaryStage.setScene(overview);
        primaryStage.show();
    }

}
