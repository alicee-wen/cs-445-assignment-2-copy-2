# **CS 0445 Assignment 2: Snake Linked Tracker**

## **🐍 Objective**

In this assignment, you will implement a linked data structure to represent a **Snake**. You must use only **plain Java linked chains** to implement the snake's body; no built-in container classes (e.g., `ArrayList`, `LinkedList`) are allowed. You will fill in the methods of `SnakeLinkedImpl.java`, which implements the provided interface `SnakeInterface<T>`.

The game engine (`SnakeGame.java`) is a **bonus** file for experimentation and visualization after implementing the core logic. It is a basic, text-based engine that supports only a single snake with no obstacles or advanced features. Bonus credit can be earned by extending the engine with additional functionality, such as a GUI, support for multiple snakes, obstacles, food mechanics, or enhanced user interaction.

> **Note:** This assignment was developed with the help of OpenAI ChatGPT to brainstorm and generate parts of the scaffolding and documentation files to speedup prototyping.

> **Note:** There are few changes to the `SnakeInterface` methods from Assignment 1. Please read the interface file and this README carefully.


---

## **📁 Folder Structure**

```
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── SnakeInterface.java
│   │       ├── SnakeCell.java
│   │       ├── SnakeLinkedImpl.java      # Your implementation goes here
│   │       └── SnakeGame.java           # Bonus game engine (text-based)
│   └── test/
│       └── java/
│           └── SnakeTest.java           # JUnit 4 test cases

```

---

## **⚙️ Compilation & Running Tests**

You can use GitHub Codespaces to run, compile, and test this assignment entirely in the cloud — no local setup required.

If you choose to work on your **local machine**, you must have Maven installed. If not:

### **Linux/macOS**

```
sudo apt install maven   # on Ubuntu/Debian
brew install maven       # on macOS

```

### **Windows**

1. Download from: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. Extract to a folder like `C:\\Program Files\\Apache\\maven`
3. Set environment variable `MAVEN_HOME` to that folder
4. Add `%MAVEN_HOME%\\bin` to your system `PATH`
5. Open a new command prompt and type `mvn -v` to verify installation

### **🔨 Compile the Project**

```
mvn compile

```

### **✅ Run Tests**

```
mvn test

```

---

## **🔍 Debugging Test Cases in VS Code with Test Runner**

To debug JUnit test cases in VS Code, follow these steps:

### **Prerequisites:**

* Install the **Java Extension Pack** in VS Code.
* You may need to install version **0.40.0** of the **Test Runner for Java** extension if debugging options do not appear.

#### **Steps:**

1. Open the test file (e.g., `SnakeTest.java`) in the editor.
2. Set breakpoints by clicking on the gutter next to the line numbers.
3. Right-click on the gutter next to the line number of the test method name and select **Debug Test**.
4. Use the debug toolbar to step through code, inspect variables, and view call stacks.

This allows you to easily verify internal state, control flow, and ensure correctness of your implementation.

---

## 🎮 Running the Snake Game Engine (Bonus)

To try the provided text-based Snake game, you have two options:

### Using Maven:

```
mvn compile
mvn exec:java -Dexec.mainClass=SnakeGame
```

### Using javac and java:

```
cd src/main/java
javac *.java
java SnakeGame
```

Use **W A S D** keys followed by Enter to move the snake up, left, down, and right. The game uses a basic command-line interface and reflects changes based on your implementation of `SnakeInterface<String>`.

> This engine is basic and supports only a single snake with no obstacles. You are encouraged to implement additional features (GUI, food, multiple snakes, etc.) for bonus credit.. You are encouraged to implement additional features (GUI, food, multiple snakes, etc.) for bonus credit.

---

## **📘 Interface and Method Details**

### `initialize(T value, int x, int y)`

Initializes the snake with one head cell at coordinates `(x, y)` containing the provided value. This method must be called before any other. All other methods should throw an `IllegalStateException` if `initialize()` has not yet been called.

### `grow(T value)`

Adds a placeholder cell to the tail, which eventually shares the same position as the current tail and remains inactive until the next call to `move()`.
If `grow()` is called multiple times in a row, each call adds a placeholder at the tail. These placeholders are activated one at a time with each subsequent `move()` call, extending the snake incrementally. `grow()` should only reserve a placeholder for a future tail cell. `move()` should be responsible for shifting the snake and making that placeholder part of the active body.

You may maintain a counter to track the number of inactive placeholders and decrement it after each `move()` to ensure accurate behavior.

### `shrink()`

Removes the current tail cell. If only one cell remains, this operation has no effect. If `shrink()` is called repeatedly, it will continue removing the tail until only one cell remains. Further calls will have no effect — the snake must always contain at least one cell.

### `insertAt(int index, T value)`

Inserts a new cell at the specified index. The inserted cell copies the coordinates of the cell currently at `index`, and all subsequent cells are shifted toward the tail.

* **Tail Extrapolation:** In all insertion cases, including inserting at the tail, the new tail follows the same direction as the last two cells. When inserting at the tail using `insertAt(index, cell)` and the snake contains only **one cell**, there are not enough points to infer a direction from two consecutive cells. In this case, the tail direction is assumed to be **to the right**. That is, the new tail cell will be one step to the right of the existing cell (i.e., `x + 1`, same `y`).
* **Invalid Input:** If the index is less than 0 or greater than the snake’s current length, throw an `IndexOutOfBoundsException`.

### `removeAt(int index)`

Removes the cell at the specified index and shifts all following cells toward the head to maintain continuity.

* **Invalid Input:** If the index is less than 0 or greater than or equal to the snake’s length, throw an `IndexOutOfBoundsException`.
* **Edge Case:** If the snake contains only one cell, `removeAt()` has no effect.

### `getAt(int i)`

Returns the `SnakeCell<T>` at index `i`.

* **Invalid Input:** If `i` is out of range, throw an `IndexOutOfBoundsException`.

### `length()`

Returns the total number of **active** cells currently in the snake.

### `move(String direction)`

Moves the head one cell in the given direction (`"U"`, `"D"`, `"L"`, `"R"`). The rest of the snake shifts toward the tail, implemented by copying each cell's position from its predecessor (i.e., `body[i] <- body[i - 1]`). A new head is created at the correct location. If a placeholder tail was added via `grow()`, it remains in place but becomes active, meaning it will be moved and counted as part of the snake’s body in subsequent operations. If the new head position would collide with any of the snake's existing cells (i.e., self-collision), the move is not performed and this method returns false.

### `checkCollision(int x, int y)`

Returns `true` if any cell in the snake occupies the coordinate `(x, y)`; returns `false` otherwise.

---

## ✏️ Implementation Header

Use the following public class header for your implementation:

```java
public class SnakeLinkedImpl<T> implements SnakeInterface<T> { ... }
```

---
#### 🧭 Coordinate System and Movement Directions

- The snake operates on a 2D plane where each cell is defined by an `(x, y)` coordinate.
- Movement directions are defined with respect to the coordinate system as follows:
  - ⬆️ **UP**: decreases the `y` coordinate (i.e., `y = y - 1`)
  - ⬇️ **DOWN**: increases the `y` coordinate (i.e., `y = y + 1`)
  - ⬅️ **LEFT**: decreases the `x` coordinate (i.e., `x = x - 1`)
  - ➡️ **RIGHT**: increases the `x` coordinate (i.e., `x = x + 1`)
- ✅ Both positive and negative values of `x` and `y` are valid.
- 🐍 The snake may move into negative coordinates if that is a valid move in the game logic.

---

### 🔁 Behavior of `grow` and `move` with `insertAt` and `removeAt`

- 🧩 The snake supports dynamic modification through `insertAt(index, cell)` and `removeAt(index)`, which allow inserting or removing cells at arbitrary positions.
- 🌱 The `grow()` method causes the next `move()` to **not** remove the tail, resulting in the snake growing by one cell in the current movement direction.
- ➕ If `insertAt()` is called after `grow()` but before `move()`, the snake will immediately grow by one cell as a result of the `insertAt` call and will still grow by one cell upon the call to `move`, but the location of the tail will be affected by the inserted cell.
- ➖ If `removeAt()` is called after `grow()` but before `move()`, the snake will immediately shrink by one cell as a result of the `removeAt` call and will still grow by one cell upon the call to `move`, but the location of the tail will be affected by the inserted cell.
---

## **🚫 Restrictions**

Please adhere to the following constraints for this assignment:

* ✅ **You may only modify one file**: `SnakeLinkedImpl.java` This is the only file you are required to change. All other files are provided for context, testing, or as a bonus.
* 💡 **Bonus (optional)**: You may modify `SnakeGame.java` if you wish to implement and test the full game logic once your snake implementation is complete. This is **not required** for full credit but will count toward bonus points if you demonstrate a working game.
* ❌ **You may NOT use any Java container classes**, such as:

  * `ArrayList`, `LinkedList` (these are just examples)
  * `HashMap`, `HashSet`
  * `Queue`, `Deque`
    Only **plain Java linked chain** is allowed. You must manage node allocation, shifting, and access logic manually.
* ⚠️ Violating these restrictions may result in a **zero** for the implementation portion of the assignment.

---

## **🧪 Testing**

Tests are in `SnakeTest.java`. To run them:

```
mvn test

```

Add more test cases as needed to verify your implementation thoroughly.

---

## **🏁 Bonus**

`SnakeGame.java` is a working text-based game engine that uses your `SnakeInterface<String>` implementation to simulate snake movement, growth, collisions, and food. Complete your class to try it out!

Bonus credit may be awarded for enhancing the game with:

* A GUI frontend
* Multiple snakes
* Obstacles
* Advanced scoring or win conditions
* Food generation logic

---

## **📊 Grading Rubric**

| FeaturePoints                                           |         |
| ------------------------------------------------------- | ------- |
| `initialize`, `getAt`, and `length()`                   | 15      |
| `grow()` and `shrink()`                                 | 20      |
| `insertAt()` and tail extrapolation                     | 15      |
| `removeAt()`                                            | 15      |
| `move()` logic and correct shifting                     | 15      |
| `checkCollision()`                                      | 10      |
| Code style, comments, and modularity                    | 10      |
| **Total (excluding bonus)**                             | **100** |
| Bonus (above 100): Playable game with advanced features | 10      |

### **💡 Grading Guidelines**

* The first six features listed above will be graded using Gradescope’s autograder.
* Test cases include both visible and hidden scenarios to assess correctness, edge handling, and boundary conditions.
* If your autograder score is below 60%, your code will be manually reviewed for partial credit.

  * However, **manual grading can award no more than 60% of the total autograder points**.
* `Code style, comments, and modularity` is graded manually and includes:

  * Clear and meaningful variable/method names
  * Proper indentation and formatting
  * Use of helper methods to reduce duplication
  * Inline comments explaining non-obvious logic
  * Adherence to Java naming conventions
* Bonus points are awarded only if the game runs with additional functionality such as GUI, multiple snakes, obstacles, or food.
