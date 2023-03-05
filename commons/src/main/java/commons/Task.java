package commons;

import java.util.Objects;

// Task class
public class Task {

    private String name;
    private String description;
    private TaskList parentTaskList;
    private Task parentTask;

    // default constructor
    public Task(String name, String description, TaskList taskList, Task parentTask) {
        if (name == null || taskList == null)
            throw new IllegalArgumentException("Name and parent TaskList must not be null");
        this.name = name;
        this.parentTaskList = taskList;
        
        if (description == null)
            this.description = "Description goes here";
        
        this.parentTask = parentTask;
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
        return name.equals(task.name) &&
            Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
