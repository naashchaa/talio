package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TaskList")
public class TaskList {

    @ManyToOne
    @JoinColumn(name = "Board_id", nullable = false)
    private Board parentBoard;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;



    TaskList(String name, Board parentBoard){
        if(name == null || parentBoard == null){
            throw new IllegalArgumentException("Name and parent board must not be null");
        }
        this.name = name;
        this.parentBoard = parentBoard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Task name cannot be null");
        }
        this.name = name;
    }

    public Board getParentBoard() {
        return parentBoard;
    }

    public void setParentBoard(Board parentBoard) {
        if(parentBoard == null){
            throw new IllegalArgumentException("parent board cannot be null");
        }
        this.parentBoard = parentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList taskList = (TaskList) o;
        return name.equals(taskList.name) && parentBoard.equals(taskList.parentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "parentBoard=" + parentBoard +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}