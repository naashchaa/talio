package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class TemporalCtrl {

    private Stage primaryStage;
    private AddTaskCtrl addtaskCtrl;
    private Scene overview;

    public void initialize(Stage primaryStage, Pair<AddTaskCtrl, Parent> addtask) {
        this.primaryStage = primaryStage;
        this.addtaskCtrl = addtask.getKey();
        this.overview = new Scene(addtask.getValue());
        primaryStage.setTitle("Task Overview");
        primaryStage.setScene(overview);
        primaryStage.show();
    }

}
