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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    public static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL stylingPath = this.getClass().getResource("../client/styling/styling.css");
        if (stylingPath == null) {
            throw new IOException("Could not find styling.css");
        }
        String css = stylingPath.toExternalForm();
        var connectToServer =
            FXML.load(ConnectToServerCtrl.class, "client", "scenes", "ConnectToServer.fxml");
        var applicationOverview =
                FXML.load(ApplicationOverviewCtrl.class, "client", "scenes", "Talio.fxml");
        var addTask =
            FXML.load(AddTaskCtrl.class, "client", "scenes", "AddTask.fxml");
        var addTaskList =
            FXML.load(AddTaskListCtrl.class, "client", "scenes", "AddTaskList.fxml");
        var editTaskList =
            FXML.load(EditTaskListCtrl.class, "client", "scenes", "EditTaskList.fxml");
        var editTask =
            FXML.load(EditTaskCtrl.class, "client", "scenes", "EditTask.fxml");
        var deleteTaskList =
            FXML.load(DeleteTaskListCtrl.class, "client", "scenes", "DeleteTaskList.fxml");
        var createBoard =
            FXML.load(CreateBoardCtrl.class, "client", "scenes", "CreateBoard.fxml");
        var adminLogin =
                FXML.load(AdminLoginCtrl.class, "client", "scenes", "AdminLogin.fxml");
        var deleteBoard =
                FXML.load(DeleteBoardCtrl.class, "client", "scenes", "DeleteBoard.fxml");
        var editBoard =
                FXML.load(EditBoardCtrl.class, "client", "scenes", "EditBoard.fxml");
        var removeBoard =
                FXML.load(LeaveBoardCtrl.class, "client", "scenes", "LeaveBoard.fxml");
        var joinBoard =
                FXML.load(JoinBoardCtrl.class, "client", "scenes", "JoinBoard.fxml");
        var joinKey =
                FXML.load(JoinKeyCtrl.class, "client", "scenes", "JoinKey.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        List<Pair<?, Parent>> scenes = List.of(
            connectToServer,
            applicationOverview,
            addTaskList,
            addTask,
            editTaskList,
            editTask,
            deleteTaskList,
            createBoard,
            adminLogin,
            deleteBoard,
            editBoard,
            removeBoard,
            joinBoard,
            joinKey
        );

        for (var scene : scenes) {
            scene.getValue().getStylesheets().add(css);
        }

        mainCtrl.initialize(primaryStage, scenes);

        primaryStage.setOnCloseRequest(e -> {
            applicationOverview.getKey().stop();
        });
    }
}