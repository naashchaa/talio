package commons;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
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

    @SuppressWarnings("unused")
    private Board(){}

    public Board(@NotNull String name) {
        this.name = name;
    }

//    constructor for when a password is entered
    public Board(@NotNull String name, @NotNull String password){
        this.name = name;
        this.password = password;
    }

    public long getId(){
        return this.id;
    }

    //only to be used for testing
    public void setId(@NotNull long l){this.id = l;}

    public String getName() {
        return this.name;
    }

//    input will probably have to be sanitized
    public void setName(@NotNull String name){
        this.name = name;
    }

    public String getPassword(){
        return this.password;
    }

//     input will probably have to be sanitized
    public void setPassword(@NotNull String password){
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(this.id, board.id) && Objects.equals(this.name, board.name)
                && Objects.equals(this.password, board.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.password);
    }

    @Override
    public String toString(){
        return "Board: " + this.name + ", " + this.id;
    }
}
