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

    private TestTaskListRepository repo;
    private TaskListController sut;

    @BeforeEach
    public void setup() {
        repo = new TestTaskListRepository();
        sut = new TaskListController(repo);
    }

    @Test
    public void testNullTaskListName(){
        Board a = new Board("a");
        TaskList b = new TaskList(null, a);
        var actual = sut.add(b);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }


    @Test
    public void testGetAll() {
        Board a = new Board("a");
        TaskList b = new TaskList("b", a);
        sut.add(b);
        List<TaskList> taskLists = new ArrayList<>();
        taskLists.add(b);
        assertEquals(taskLists, sut.getAll());
    }

    @Test
    public void testGetById() {
        Board a = new Board("a");
        TaskList b1 = new TaskList("b1", a);
        TaskList b2 = new TaskList("b2", a);
        TaskList b3 = new TaskList("b3", a);
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