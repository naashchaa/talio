package commons;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TaskList")
public class TaskList {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "Board_id", nullable = false)

    private Board parentBoard;

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 50)
    private String name;

    @SuppressWarnings("unused")
    TaskList(){}

    public TaskList(@NotNull String name, @NotNull Board parentBoard){
        this.name = name;
        this.parentBoard = parentBoard;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long l){this.id = l;}

    public Board getParentBoard() {
        return this.parentBoard;
    }

    public void setParentBoard(@NotNull Board parentBoard) {
        this.parentBoard = parentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList taskList = (TaskList) o;
        return this.id == taskList.id &&
                Objects.equals(this.parentBoard, taskList.parentBoard)
                && Objects.equals(this.name, taskList.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.parentBoard, this.id, this.name);
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "parentBoard= " + this.parentBoard.getName() +
                ", id= " + this.id +
                ", name= '" + this.name + '\'' +
                '}';
    }
}

