package commons;

import java.util.List;

public interface TaskList {

    String getName(); // returns the TaskList name

    void setName(); // sets the TaskList name

    List<Task> getTasks(); // returns a list of all Tasks in the TaskList

    void addTask(Task task); // adds a Task to the list

    // returns and removes a task if it is present, does nothing and returns null otherwise
    Task removeTask(Task task);

    Board getParentBoard(); // returns the parent Board

    void setParentBoard(Board parent); // sets the parent board

}
