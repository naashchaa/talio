package server.api;

import commons.Board;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class TaskListControllerTest {

    private TaskListController sut;

    @BeforeEach
    public void setup() {
        sut = new TaskListController(new TestTaskListRepository());
    }

    @Test
    void getAll() {
        Board a = new Board("a");
        TaskList b = new TaskList("b", a);
        sut.add(b);
        List<TaskList> taskLists = new ArrayList<>();
        taskLists.add(b);
        assertEquals(taskLists, sut.getAll());
    }

    @Test
    void getById() {
        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);
        TaskList b2 = new TaskList("b2", a);
        TaskList b3 = new TaskList("b3", a);
        sut.add(b1);
        sut.add(b2);
        sut.add(b3);
        assertEquals(b1, sut.getById(1).getBody());
        assertEquals(b2, sut.getById(2).getBody());
        assertEquals(b3, sut.getById(3).getBody());
        assertEquals(BAD_REQUEST, sut.getById(5).getStatusCode());
        assertEquals(BAD_REQUEST, sut.getById(-1).getStatusCode());
    }

    @Test
    void getAllByParentBoard() {
        Board a = new Board("a");
        a.setId(1);
        Board c = new Board("c");
        c.setId(2);
        TaskList b1 = new TaskList("b1", a);
        TaskList b2 = new TaskList("b2", a);
        TaskList b3 = new TaskList("b3", c);

        List<TaskList> lists = new ArrayList<>();
        lists.add(b1);
        lists.add(b2);

        sut.add(b1);
        sut.add(b2);
        sut.add(b3);

        assertEquals(lists, sut.getAllByParentBoard(1).getBody());
    }

    @Test
    void add() {
        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);
        TaskList b2 = b1;

        b1 = sut.add(b1).getBody();

        assertEquals(b2, b1);
    }

    @Test
    void delete() {
        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);

        b1 = sut.add(b1).getBody();
        assertNotNull(b1);
        assertEquals(b1, sut.getById(b1.getId()).getBody());

        b1 = sut.delete(b1.getId()).getBody();
        assertNotNull(b1);
        assertEquals(BAD_REQUEST, sut.getById(b1.getId()).getStatusCode());

        assertEquals(BAD_REQUEST, sut.delete(b1.getId()).getStatusCode());
    }

    @Test
    void update() {

        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);
        TaskList b0 = new TaskList("b1", a);
        TaskList b2 = new TaskList("b2", a);
        TaskList b3 = new TaskList("b3", a);

        b1 = sut.add(b1).getBody();
        b2 = sut.add(b2).getBody();
        assertNotNull(b1);
        assertNotNull(b2);

        b1.setName("b2");
        b1 = sut.update(b1).getBody();
        assertNotNull(b1);

        assertEquals(BAD_REQUEST, sut.update(b3).getStatusCode());

        assertNotEquals(b1, b0);
        assertEquals(b1.getName(), b2.getName());
    }

    @Test
    void addTaskList() {
        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);
        TaskList b2 = b1;

        b1 = sut.addTaskList(b1);

        assertEquals(b2, b1);
    }

    @Test
    void getUpdates() {
        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);

        sut.add(b1);

        var result = sut.getUpdates();

        assertNotNull(result);
    }

    @Test
    void addMessage() {
        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);
        TaskList b2 = b1;

        b1 = sut.addMessage(b1);

        assertEquals(b2, b1);
    }

    @Test
    void editMessage() {
        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);
        TaskList b0 = new TaskList("b1", a);
        TaskList b2 = new TaskList("b2", a);

        b1 = sut.add(b1).getBody();
        b2 = sut.add(b2).getBody();
        assertNotNull(b1);
        assertNotNull(b2);

        b1.setName("b2");
        b1 = sut.editMessage(b1);
        assertNotNull(b1);

        assertNotEquals(b1, b0);
        assertEquals(b1.getName(), b2.getName());
    }

    @Test
    void deleteMessage() {
        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);

        b1 = sut.add(b1).getBody();
        assertNotNull(b1);
        assertEquals(b1, sut.getById(b1.getId()).getBody());

        b1 = sut.deleteMessage(b1);
        assertNotNull(b1);
        assertEquals(BAD_REQUEST, sut.getById(b1.getId()).getStatusCode());
    }
}