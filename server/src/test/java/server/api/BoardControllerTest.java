package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BoardControllerTest {

    private TestBoardRepository repo;
    private BoardController sut;

    @BeforeEach
    public void setup(){
        repo = new TestBoardRepository();
        sut = new BoardController(repo);
    }

    @Test
    public void cannotAddNullBoardName(){
        var actual = sut.add(new Board(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void testGetAll(){
        Board b = new Board("b");
        sut.add(b);
        List<Board> boards = new ArrayList<>();
        boards.add(b);
        assertEquals(boards, sut.getAll());
    }

    @Test
    public void testGetById(){
        Board b1 = new Board("b1");
        Board b2 = new Board("b2");
        Board b3 = new Board("b3");
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        assertEquals(b1, sut.getById(0).getBody());
        assertEquals(b2, sut.getById(1).getBody());
        assertEquals(b3, sut.getById(2).getBody());
        assertEquals(BAD_REQUEST, sut.getById(5).getStatusCode());
        assertEquals(BAD_REQUEST, sut.getById(-1).getStatusCode());
    }

}
