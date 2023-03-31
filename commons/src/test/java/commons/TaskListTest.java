package commons;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {
    Board b1 = new Board("a");
    Board b2 = new Board("b");
    TaskList a1 = new TaskList("a", b1);
    TaskList a2 = new TaskList("a", b1);
    TaskList a3 = new TaskList("b", b1);
    TaskList a4 = new TaskList("a", b2);
    TaskList a5 = a1;


    @Test
    void testName() {
        assertEquals("a", a1.getName());
        a1.setName("aa");
        assertEquals("aa", a1.getName());
    }

    @Test
    void testId() {
        assertEquals(0, a1.getId());
        a1.setId(1);
        assertEquals(1, a1.getId());
    }

    @Test
    void testParentBoard() {
        assertEquals(b1, a1.getParentBoard());
        a1.setParentBoard(b2);
        assertEquals(b2, a1.getParentBoard());
    }

    @Test
    void testEquals() {
        assertEquals(a1, a2);
        assertNotEquals(a1, a3);
        assertNotEquals(a1, a4);
        assertEquals(a1, a5);
    }



    @Test
    void testHashCode() {
        Long id = a1.getId();
        String name = a1.getName();
        int expectedHash = Objects.hash(b1, id, name);
        assertEquals(expectedHash, a1.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("TaskList{parentBoard= a, id= 0, name= 'a'}", a1.toString());
    }
}