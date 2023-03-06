package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String password;

    private Board(){
        // for object mapper
    }

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

    public long getID(){
        return this.id;
    }

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
}
