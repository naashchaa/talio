package server.api;

import commons.Board;
import commons.Task;
import commons.TaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class TaskControllerTest {
    private TestTaskRepository repository;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        repository = new TestTaskRepository();
        taskController = new TaskController(repository);
    }

    @Test
    void getAll() {
        Board board = new Board("board");
        TaskList taskList1 = new TaskList("taskList1", board);
        TaskList taskList2 = new TaskList("taskList2", board);
        Task task1 = new Task("task1", null, taskList1, null);
        Task task2 = new Task("task2", null, taskList2, null);
        Task task3 = new Task("task3", null, taskList2, null);

        List<Task> list1 = new ArrayList<>();
        list1.add(task1);
        list1.add(task2);
        list1.add(task3);

        taskController.add(task1);
        taskController.add(task2);
        taskController.add(task3);

        List<Task> list2 = taskController.getAll();
        assertEquals(list1, list2);
    }

    @Test
    void getById() {
        Board board = new Board("board");
        TaskList taskList = new TaskList("taskList", board);
        Task task1 = new Task("task1", null, taskList, null);
        Task task2 = new Task("task2", null, taskList, null);
        taskController.add(task1);
        taskController.add(task2);
        assertEquals(task1, taskController.getById(task1.getId()).getBody());
    }

    @Test
    void getAllByParentTaskList() {
        Board board = new Board("board");
        TaskList taskList1 = new TaskList("taskList1", board);
        TaskList taskList2 = new TaskList("taskList2", board);
        Task task1 = new Task("task1", null, taskList1, null);
        Task task2 = new Task("task2", null, taskList2, null);

        taskController.add(task1);
        taskController.add(task2);
        var list = new ArrayList<>();
        list.add(task1);

        assertEquals(list, taskController.getAllByParentTaskList(taskList1.getId()).getBody());
    }

    @Test
    void add() {
        Board board = new Board("board");
        TaskList taskList = new TaskList("taskList", board);
        Task task = new Task("task1", null, taskList, null);

        assertEquals(BAD_REQUEST, taskController.getById(task.getId()).getStatusCode());
        taskController.add(task);
        assertEquals(task, taskController.getById(task.getId()).getBody());
    }

    @Test
    void update() {
        Board board = new Board("board");
        TaskList taskList = new TaskList("taskList", board);
        Task task1 = new Task("task1", null, taskList, null);
        Task task2 = new Task("task2", null, taskList, null);

        taskController.add(task1);
        assertEquals(task1, taskController.getById(task1.getId()).getBody());
        taskController.update(task1.getId(), task2);
        assertEquals(task2, taskController.getById(task1.getId()).getBody());
    }

    @Test
    void delete() {
        Board board = new Board("board");
        TaskList taskList = new TaskList("taskList", board);
        Task task1 = new Task("task1", null, taskList, null);
        Task task2 = new Task("task2", null, taskList, null);
        taskController.add(task1);
        taskController.add(task2);
        assertEquals(task1, taskController.getById(task1.getId()).getBody());
        taskController.delete(task1.getId());
        assertEquals(BAD_REQUEST, taskController.getById(task1.getId()).getStatusCode());
        assertEquals(task2, taskController.getById(task2.getId()).getBody());
    }
}