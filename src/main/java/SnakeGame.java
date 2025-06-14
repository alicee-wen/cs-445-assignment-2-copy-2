// Very Basic Text-Based Snake Game Engine

import java.util.Scanner;

public class SnakeGame {
    public static void main(String[] args) {
        SnakeInterface<String> snake = new SnakeLinkedImpl<>();
        snake.initialize("H", 5, 5);
        snake.grow("1");
        snake.grow("2");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Use W/A/S/D to move. Q to quit.");
        printBoard(snake);
        while (true) {
            String dir = scanner.nextLine().toUpperCase();
            if (dir.equals("Q"))
                break;

            switch (dir) {
                case "W":
                    snake.move("U");
                    break;
                case "A":
                    snake.move("L");
                    break;
                case "S":
                    snake.move("D");
                    break;
                case "D":
                    snake.move("R");
                    break;
                default:
                    System.out.println("Invalid input");
                    continue;
            }

            printBoard(snake);
        }
        scanner.close();
    }

    private static void printBoard(SnakeInterface<String> snake) {
        int size = 10;
        String[][] board = new String[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                board[i][j] = ".";

        for (int i = 0; i < snake.length(); i++) {
            SnakeCell cell = (SnakeCell) snake.getAt(i);
            if (cell.x >= 0 && cell.x < size && cell.y >= 0 && cell.y < size)
                board[cell.y][cell.x] = (i == 0) ? "H" : "o";
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }
    }
}
