package commons;

import java.util.Objects;

// Task class
public class Task {

    private String name;
    private String description;
    private TaskList parentTaskList;
    private Task parentTask;

    /** The constructor for the Task class.
     * @param name "name", content or header of the task.
     * @param description Task description. It is an advanced feature,
     *                    usage of it is not required yet.
     * @param taskList Reference to the parent TaskList, aka the TaskList this Task belongs to.
     * @param parentTask Reference to the ParentTask.
     *                   This is an advanced feature from the "nested tasks" rubric,
     *                   and is therefore not needed and can be set to null.
     */
    // default constructor
    public Task(String name, String description, TaskList taskList, Task parentTask) {
        if (name == null || taskList == null)
            throw new IllegalArgumentException("Name and parent TaskList must not be null");
        this.name = name;
        this.parentTaskList = taskList;

        if (description == null)
            this.description = "Description goes here";
    }


    //empty constructor for the object mapper
    @SuppressWarnings("unused")
    private Task(){
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        // this could potentially be very troublesome, 
        // input will have to be sanitized somewhere more thoroughly
        if (name == null)
            throw new IllegalArgumentException("Task name cannot be null");
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    /** This method sets the description of the Task. It is an advanced feature,
     * and therefore doesn't need to be used at the basic level.
     * @param description The string with the description
     */
    public void setDescription(String description) {
        if (description == null) {
            this.description = "Description goes here";
        }
        else {
            this.description = description;
        }
    }

    public TaskList getParentTaskList() {
        return this.parentTaskList;
    }

    /** This method sets the parent TaskList of a Task. It is your responsibility
     * To make sure the specified TaskList actually exists.
     * @param parent Reference to the parent TaskList
     */
    public void setParentTaskList(TaskList parent) {
        // validation for an existing task list should be done elsewhere
        if (parent == null) {
            throw new IllegalArgumentException("parent cannot be null");
        }
        else {
            this.parentTaskList = parent;
        }
    }

    public Task getParentTask() {
        return this.parentTask;
    }

    public void setParentTask(Task parent) {
        this.parentTask = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return this.name.equals(task.name) &&
                Objects.equals(this.description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.description);
    }
}
