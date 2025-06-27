import org.junit.Test;
import static org.junit.Assert.*;

public class IndividualTest {

    @Test
    public void testInitializeAndLength() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("A", 0, 0);
        assertEquals(1, snake.length());
        assertEquals("A", snake.getAt(0).value);
        assertEquals(0, snake.getAt(0).x);
        assertEquals(0, snake.getAt(0).y);
    }

    @Test
    public void testGrowAlone() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("H", 1, 1);
        snake.grow("T");  // pending only, not added to snake body yet
        assertEquals(1, snake.length());  // still 1 until move is called
    }

    @Test
    public void testShrink() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("A", 0, 0);
        snake.grow("B");
        snake.insertAt(1, "B"); // simulate growing immediately
        snake.shrink();
        assertEquals(1, snake.length());
    }

    @Test
    public void testInsertAtAndRemoveAt() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("A", 0, 0);
        snake.insertAt(1, "B");
        assertEquals(2, snake.length());
        assertEquals("B", snake.getAt(1).value);

        snake.removeAt(1);
        assertEquals(1, snake.length());
    }

    @Test
    public void testGetAt() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("X", 1, 2);
        SnakeCell<String> cell = snake.getAt(0);
        assertEquals("X", cell.value);
        assertEquals(1, cell.x);
        assertEquals(2, cell.y);
    }

    @Test
    public void testMoveAndCollision() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("H", 0, 0);
        assertTrue(snake.move("R"));
        assertTrue(snake.checkCollision(0, 0));
    }

    @Test(expected = IllegalStateException.class)
    public void testLengthBeforeInitialization() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.length();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetAtInvalidIndex() {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("H", 0, 0);
        snake.getAt(1); // out of bounds
    }
}
