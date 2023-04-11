package commons;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board b1 = new Board("b");
    Board b2 = new Board("b");
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
        b2.setPassword("testpw");
        assertNotNull(b2.getPassword());
        assertEquals("testpw", b2.getPassword());
        b2.setPassword("testpw2");
        assertNotNull(b2.getPassword());
        assertEquals("testpw2", b2.getPassword());
    }

    @Test
    void testHashEquals() {
        b1.setId(1L);
        assertEquals(b1, b1);
        assertEquals(b1, b2);
        assertNotEquals(b1, b4);
        assertEquals(b1, b5);

        assertEquals(b1.hashCode(), b1.hashCode());
        assertNotEquals(b1.hashCode(), b2.hashCode());
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