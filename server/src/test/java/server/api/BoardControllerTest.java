package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BoardControllerTest {

    private BoardController sut;

    @BeforeEach
    public void setup(){
        sut = new BoardController(new TestBoardRepository());
    }

    @Test
    void getAll() {
        Board b = new Board("b");
        sut.add(b);
        List<Board> boards = new ArrayList<>();
        boards.add(b);
        assertEquals(boards, sut.getAll());
    }

    @Test
    void getById() {
        Board b1 = new Board("b1");
        Board b2 = new Board("b2");
        Board b3 = new Board("b3");
        b1 = sut.add(b1).getBody();
        b2 = sut.add(b2).getBody();
        b3 = sut.add(b3).getBody();
        assertNotNull(b1);
        assertNotNull(b2);
        assertNotNull(b3);
        assertEquals(b1, sut.getById(1).getBody());
        assertEquals(b2, sut.getById(2).getBody());
        assertEquals(b3, sut.getById(3).getBody());
        assertEquals(BAD_REQUEST, sut.getById(5).getStatusCode());
        assertEquals(BAD_REQUEST, sut.getById(-1).getStatusCode());
    }

    @Test
    void add() {
        Board b = new Board("b");
        b = sut.add(b).getBody();
        assertNotNull(b);
        assertEquals(b, sut.getById(b.getId()).getBody());
    }

    @Test
    void delete() {
        Board b = new Board("b");
        b = sut.add(b).getBody();
        assertNotNull(b);
        b = sut.delete(b.getId()).getBody();
        assertNotNull(b);
        assertEquals(BAD_REQUEST, sut.delete(b.getId()).getStatusCode());
    }

    @Test
    void update() {
        Board b = new Board("b");
        Board c = new Board("c");
        b = sut.add(b).getBody();
        assertNotNull(b);
        b.setName("bb");
        b = sut.update(b).getBody();
        assertNotNull(b);
        assertEquals("bb", b.getName());
        assertEquals(BAD_REQUEST, sut.update(c).getStatusCode());
    }
}
