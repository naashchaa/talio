package commons;

import org.jetbrains.annotations.NotNull;
import javax.persistence.*;
import java.util.Objects;

// Task class
@Entity
@Table(name = "Task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    public long id;
    @Column(nullable = false)
    private String name;
    @Column
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "TaskList_id")
    private TaskList parentTaskList;

    @OneToOne
    private Task nextTask;
    @OneToOne
    private Task prevTask;

    @ManyToOne
    @JoinColumn(name = "Task_id")
    private Task parentTask;

    /** The constructor for the Task class.
     * @param name "name", content or header of the task.
     * @param description Task description. It is an advanced feature,
     *                    usage of it is not required yet.
     * @param parentTaskList Reference to the parent TaskList this Task belongs to.
     * @param parentTask Reference to the ParentTask.
     *                   This is an advanced feature from the "nested tasks" rubric,
     *                   and is therefore not needed and can be set to null.
     */
    public Task(@NotNull String name, String description,
                @NotNull TaskList parentTaskList, Task parentTask) {
        this.name = name;
        this.description = description;
        this.parentTaskList = parentTaskList;
        this.parentTask = parentTask;
        this.nextTask = null;
        this.prevTask = null;
    }

    //empty constructor for the object mapper
    @SuppressWarnings("unused")
    Task() {
    }

    //empty constructor for hibernate???


    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(@NotNull String name) {
        if (name.equals(""))
            throw new IllegalArgumentException("Task name cannot be empty");
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
        this.description = description;
    }
    
    public TaskList getParentTaskList() {
        return this.parentTaskList;
    }

    /** This method sets the parent TaskList of a Task. It is your responsibility
     * To make sure the specified TaskList actually exists.
     * @param parent Reference to the parent TaskList
     */
    public void setParentTaskList(@NotNull TaskList parent) {
        // validation for an existing task list should be done elsewhere
        this.parentTaskList = parent;
    }
    
    public Task getParentTask() {
        return this.parentTask;
    }
    
    public void setParentTask(Task parent) {
        this.parentTask = parent;
    }

    public Task getNextTask() {
        return this.nextTask;
    }

    public void setNextTask(Task next) {
        this.nextTask = next;
    }

    public Task getPrevTask() {
        return this.prevTask;
    }

    public void setPrevTask(Task prev) {
        this.prevTask = prev;
    }

    public void setId(long id) {
        this.id = id;
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
