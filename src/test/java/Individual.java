import org.junit.Test;
import static org.junit.Assert.*;

public class Individual {

    @Test
    public void testInsertAtSize2Cases() {
        SnakeLinkedImpl<String> snake = new SnakeLinkedImpl<>();
        
        snake.initialize("H", 0, 0);
        printSnake(snake);

        snake.insertAt(1, "T1"); // size == 2, add at end
        printSnake(snake);

        snake.insertAt(0, "NEW_HEAD"); // size == 2, add at head
        printSnake(snake);

        snake.insertAt(1, "MIDDLE"); // size == 3, insert in middle
        printSnake(snake);

        snake.insertAt(2, "MIDDLE2"); // insert further into middle
        printSnake(snake);

        snake.insertAt(snake.length(), "TAIL_EXTRA"); // insert at end with size > 2
        printSnake(snake);
    }

    private void printSnake(SnakeLinkedImpl<String> snake) {
        System.out.println("Snake state (length=" + snake.length() + "):");
        for (int i = 0; i < snake.length(); i++) {
            SnakeCell<String> cell = snake.getAt(i);
            System.out.println(i + ": (" + cell.x + "," + cell.y + ") " + cell.value);
        }
        System.out.println();
    }
}
