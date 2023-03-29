package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    private static final Board TEST_BOARD = new Board("board");
    private static final TaskList TEST_TASKLIST = new TaskList("task list", TEST_BOARD.getId());
    private static final Task TEST_TASK = new Task("task", "description",
                                                    TEST_TASKLIST, null);

    @Test
    void testGetName() {
        assertEquals("task", TEST_TASK.getName());
    }

    @Test
    void testSetNameValid() {
        TEST_TASK.setName("cool task");
        assertEquals("cool task", TEST_TASK.getName());
        TEST_TASK.setName("task");
    }

    @Test
    void testSetNameEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {TEST_TASK.setName("");});
    }

    @Test
    void testSetNameNull() {
        assertThrows(NullPointerException.class, () -> {TEST_TASK.setName(null);});
    }

    @Test
    void getDescription() {
        assertEquals("description", TEST_TASK.getDescription());
    }

    //description is allowed to be null at this stage,
    //further tests for when it won't be might be needed
    @Test
    void setDescriptionValid() {
        TEST_TASK.setDescription("cool description");
        assertEquals("cool description", TEST_TASK.getDescription());
        TEST_TASK.setDescription("description");
    }

    @Test
    void getParentTaskList() {
        assertEquals(TEST_TASKLIST, TEST_TASK.getParentTaskList());
    }

    @Test
    void setParentTaskList() {
        TaskList list2 = new TaskList("task list 2", TEST_BOARD.getId());
        TEST_TASK.setParentTaskList(list2);
        assertEquals(list2, TEST_TASK.getParentTaskList());
        TEST_TASK.setParentTaskList(TEST_TASKLIST);
    }

    //parent tasks are an advanced feature and are hence stubs
    @Test
    void getParentTask() {
        assertNull(TEST_TASK.getParentTask());
    }

    @Test
    void setParentTask() {
        TEST_TASK.setParentTask(TEST_TASK);
        assertEquals(TEST_TASK, TEST_TASK.getParentTask());
        TEST_TASK.setParentTask(null);
    }

    @Test
    void testEquals() {
        Task task2 = new Task("task", "description", TEST_TASKLIST, null);
        assertEquals(TEST_TASK, task2);
    }

    @Test
    void testHashCode() {
        Task task2 = new Task("task", "description", TEST_TASKLIST, null);
        assertEquals(TEST_TASK.hashCode(), task2.hashCode());
    }
}
