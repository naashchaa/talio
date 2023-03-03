package server;

import java.util.List;

public interface Board {

    String getID(); // returns the unique board ID

    String getName(); // returns the Board name

    void setName(); // sets the Board name

    List<TaskList> getTaskLists(); // returns a list of all TaskLists in the Board

    void addTaskList(TaskList taskList); // adds a new TaskList to the Board

    // returns and removes a TaskList if it is present, does nothing and returns null otherwise
    TaskList removeTaskList(TaskList taskList);

    // String getPassword(String password); // gets the board password

    // void setPassword(String password); // sets the board password
}
