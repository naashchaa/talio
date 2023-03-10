package commons;

import org.jetbrains.annotations.NotNull;

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

    @SuppressWarnings("unused")
    private TaskList(){}

    TaskList(@NotNull String name, @NotNull Board parentBoard){
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
        return this.name.equals(taskList.name) && this.parentBoard.equals(taskList.parentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "parentBoard=" + this.parentBoard +
                ", id=" + this.id +
                ", name='" + this.name + '\'' +
                '}';
    }
}

