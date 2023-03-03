package commons;

// Interface for the Task entity
public interface Task {

    String getName(); // returns the Task name

    void setName(String name); // sets the Task name

    // String getDescription(); // returns the Task description

    // void setDescription(); // sets the task description

    TaskList getParentTaskList(); // returns the task list that the Task belongs to

    void setParentTaskList(TaskList parent); // sets the parent task list

    // Task getParentTask(); // returns the parent Task if the Task is nested, and null otherwise

    // void setParentTask(Task parent); // sets the parent Task for nesting

}
