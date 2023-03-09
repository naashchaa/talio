package commons;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "board")
public class Board {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    private String password;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<TaskList> taskLists = new ArrayList<>();

    public Board(String name) {
        if (name == null) throw new IllegalArgumentException("Name must not be null");
        this.name = name;
    }

//    constructor for when a password is entered
    public Board(String name, String password){
        if (name == null) throw new IllegalArgumentException("Name must not be null");
        this.name = name;
        if (password == null) throw new IllegalArgumentException("Password must not be null");
        this.password = password;
    }

    public long getId(){
        return this.id;
    }

    //only to be used for testing
    public void setId(long l){this.id = l;}

    public String getName() {
        return this.name;
    }

//    input will probably have to be sanitized
    public void setName(String name){
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        this.name = name;
    }

     public String getPassword(){
        return this.password;
     }

//     input will probably have to be sanitized
     public void setPassword(String password){
        this.password = password;
     }

     public List<TaskList> getTaskLists(){
        return this.taskLists;
     }

     public void setTaskLists(List<TaskList> list){
        this.taskLists = list;
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

    @Override
    public String toString(){
        return "Board: " + this.name + ", " + this.id;
    }
}
