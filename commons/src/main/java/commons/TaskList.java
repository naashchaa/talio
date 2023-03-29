package commons;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TaskList")
public class TaskList {

//    @ManyToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "Board_id", nullable = false)
    @Column(name = "Board_id")
    private long parentBoard;

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 50)
    private String name;

    @SuppressWarnings("unused")
    private TaskList(){}

    public TaskList(@NotNull String name, @NotNull long parentBoard){
        this.name = name;
        this.parentBoard = parentBoard;
    }

    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long l){this.id = l;}

    public long getParentBoard() {
        return this.parentBoard;
    }

    public void setParentBoard(@NotNull long parentBoard) {
        this.parentBoard = parentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskList taskList = (TaskList) o;
        return this.parentBoard == taskList.parentBoard && this.id == taskList.id && Objects.equals(this.name, taskList.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "parentBoardId= " + this.parentBoard +
                ", id= " + this.id +
                ", name= '" + this.name + '\'' +
                '}';
    }
}

