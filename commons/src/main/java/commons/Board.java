package commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {

    private final String id;
    private String name;
    private final List<TaskList> taskLists;
    private String password;

    public Board(String id, String name) {
        this.id = id;
        this.name = name;
        this.taskLists = new ArrayList<>();
    }

    public String getID(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<TaskList> getTaskLists(){
        return this.taskLists;
    }

    /**
     * Adds a TaskList to the list of TaskLists
     * @param taskList to be added to the list
     */
    public void addTaskList(TaskList taskList){
        this.taskLists.add(taskList);
    }

    /**
     * Removes a TaskList from the list of TaskLists if it exists
     * @param taskList to be removed
     * @return the removed taskList if it exists and null otherwise
     */
    public TaskList removeTaskList(TaskList taskList){
        int index = this.taskLists.indexOf(taskList);
        if (index == -1) return null;
        return this.taskLists.remove(index);
    }

     public String getPassword(){
        return this.password;
     }

     public void setPassword(String password){
        this.password = password;
     }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(this.id, board.id) && Objects.equals(this.name, board.name)
                && Objects.equals(this.taskLists, board.taskLists) && Objects.equals(this.password, board.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.taskLists, this.password);
    }
}
