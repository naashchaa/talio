package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board b1 = new Board("b");
    Board b2 = new Board("b");
    Board b3 = new Board("b", "test1");
    Board b4 = new Board("bb");
    Board b5 = b1;

    @Test
    void testId(){
        b1.setId(1L);
        assertEquals(1L, b1.getId());
    }
    @Test
    void testName() {
        assertEquals("b", b1.getName());
        b1.setName("b1");
        assertEquals("b1", b1.getName());
        b1.setName("b");
    }

    @Test
    void testPassword() {
        assertNull(b1.getPassword());
        assertNotNull(b3.getPassword());
        assertEquals("test1", b3.getPassword());
        b3.setPassword("test2");
        assertNotNull(b3.getPassword());
        assertEquals("test2", b3.getPassword());
    }

    @Test
    void getTaskLists() {
        assertEquals(0, b1.getTaskLists().size());
//        test for setting a list needed but no implementation of taskList yet
    }

    @Test
    void testHashEquals() {
        assertEquals(b1, b1);
        assertEquals(b1, b2);
        assertNotEquals(b1, b3);
        assertNotEquals(b1, b4);
        assertEquals(b1, b5);

        assertEquals(b1.hashCode(), b1.hashCode());
        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b1.hashCode(), b3.hashCode());
        assertNotEquals(b1.hashCode(), b4.hashCode());
        assertEquals(b1.hashCode(), b5.hashCode());
    }

    @Test
    void testToString(){
        assertEquals("Board: b, 0", b1.toString());
        assertEquals("Board: bb, 0", b4.toString());
        b1.setId(1L);
        assertEquals("Board: b, 1", b1.toString());
    }
}