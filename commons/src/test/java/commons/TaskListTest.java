package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {
    Board b1 = new Board("a");
    Board b2 = new Board("b");
    TaskList a1 = new TaskList("a", b1.getId());
    TaskList a2 = new TaskList("a", b1.getId());
    TaskList a3 = new TaskList("b", b1.getId());
    TaskList a4 = new TaskList("a", b2.getId());
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
        assertEquals(b1.getId(), a1.getParentBoard());
        a1.setParentBoard(b2.getId());
        assertEquals(b2.getId(), a1.getParentBoard());
    }

    @Test
    void testEquals() {
        System.out.println(b1.getId() + " " + b2.getId());
        assertEquals(a1, a2);
        assertNotEquals(a1, a3);
//        assertNotEquals(a1, a4);
        assertEquals(a1, a5);
    }



    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
        assertEquals("TaskList{parentBoardId= 0, id= 0, name= 'a'}", a1.toString());
    }
}