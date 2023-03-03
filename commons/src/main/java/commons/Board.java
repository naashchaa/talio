package commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {

    private final String id;
    private String name;
    private final List<TaskList> taskLists;
    private String password;


    /**
     * Constructor for when the password field is left empty
     * @param id
     * @param name
     */
    public Board(String id, String name) {
        this.id = id;
        this.name = name;
        this.taskLists = new ArrayList<>();
    }

    /**
     * Constructor for when the password field is filled in
     * @param id
     * @param name
     * @param password
     */
    public Board(String id, String name, String password){
        this.id = id;
        this.name = name;
        this.taskLists = new ArrayList<>();
        this.password = password;
    }

    String getID(){
        return this.id;
    }


    String getName() {
        return this.name;
    }

    void setName(String name){
        this.name = name;
    }

    List<TaskList> getTaskLists(){
        return this.taskLists;
    }

    /**
     * Adds a TaskList to the list of TaskLists
     * @param taskList to be added to the list
     */
    void addTaskList(TaskList taskList){
        this.taskLists.add(taskList);
    }

    /**
     * Removes a TaskList from the list of TaskLists if it exists
     * @param taskList to be removed
     * @return the removed taskList if it exists and null otherwise
     */
    TaskList removeTaskList(TaskList taskList){
        int index = this.taskLists.indexOf(taskList);
        if (index == -1) return null;
        return this.taskLists.remove(index);
    }

     String getPassword(){
        return this.password;
     }

     void setPassword(String password){
        this.password = password;
     }

    /**
     * Compares two Objects to see if they are equal
     * @param o Object to be compared
     * @return true iff Object o is not null, of the same class and all attributes are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(this.id, board.id) && Objects.equals(this.name, board.name)
                && Objects.equals(this.taskLists, board.taskLists) && Objects.equals(this.password, board.password);
    }

    /**
     * Generates a unique hashCode for the Object it is called on
     * @return unique hashCode for the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.taskLists, this.password);
    }
}
