import java.util.Arrays;

public class SnakeLinkedImpl<T> implements SnakeInterface<T> {

    private class Node<T> {
        private SnakeCell<T> cell; // Entry in list
        private Node<T> next; // Link to next node

        private Node(SnakeCell<T> newCell)
        {
            cell = newCell;
            next = null;	
        } // end constructor

        private Node(T newCellValue, int x, int y)
        {
            cell = new SnakeCell<>(x, y, newCellValue);
            next = null;	
        } // end constructor

        private Node(SnakeCell<T> newCell, Node<T> nextNode)
        {
            cell = newCell;
            next = nextNode;	
        } // end constructor

        private SnakeCell<T> getCell()
        {
            return cell;
        } // end getData

        private void setData(SnakeCell<T> newData)
        {
            cell = newData;
        } // end setData

        private Node<T> getNextNode()
        {
            return next;
        } // end getNextNode

        private void setNextNode(Node<T> nextNode)
        {
            next = nextNode;
        } 
        

	}
    private Node<T> head;
    private  T[] pending;
    private int size = 0;
    private int pendingGrows;
    private boolean initialized;
    
    public SnakeLinkedImpl(){
        head = null;
        size = 0;
        initialized = false;
        pendingGrows=0;
    }


     public Node<T> getNodeAt(int givenPosition)
	{
		assert !isEmpty() && (1 <= givenPosition) && (givenPosition <= size);
		Node<T> currentNode = head;
		
      // Traverse the chain to locate the desired node
      // (skipped if givenPosition is 1)
		for (int counter = 1; counter < givenPosition; counter++)
			currentNode = currentNode.getNextNode();
		
		assert currentNode != null;
      
		return currentNode;
    }

    // ==============================================================================================

    /**
    /**
     * Initializes the snake with a single head cell at the given coordinates.
     * 
     * @param headValue the value to store in the head cell
     * @param x         the x-coordinate of the head
     * @param y         the y-coordinate of the head
     */

    @Override
    public void initialize(T headValue, int x, int y){
        head = new Node(headValue, x, y);
        initialized = true;
        size = 1;
        }

    // ==============================================================================================

    /**
     * Adds a placeholder cell at the end (tail) of the snake.
     * The placeholder will be adjusted on the next
     * move call to an actual position (same as the snake's tail at
     * the time of the move call)
     * 
     * @param value the value to assign to the new tail cell
     * @throws IllegalStateException if the snake is not initialized
     */
    public void grow(T value){
        checkInitialization();
        pending[pendingGrows] = value;
        pendingGrows++;
        if (pending.length == pendingGrows) {
        pending = Arrays.copyOf(pending, pending.length * 2);
      }
    }
        


    // ==============================================================================================

    /**
     * Removes the last cell of the snake unless it is the only cell, in
     * which case nothing happens.
     * @throws IllegalStateException if the snake is not initialized
     */
    @Override
    public void shrink(){
        checkInitialization();
        if (size==1) return;

        assert !isEmpty();

        Node<T> oldTail = getNodeAt(size);
        oldTail = null;
        size--;
    }



    // ==============================================================================================

    /**
     * Inserts a new cell at the specified index in the body.
     * The rest of the body is shifted accordingly towards the tail.
     * 
     * @param index the zero-based position to insert the new cell
     * @param value the value of the new cell
     * @throws IndexOutOfBoundsException if the index is invalid
     * @throws IllegalStateException if the snake is not initialized
     */
    public void insertAt(int index, T value){
        
        checkInitialization();
        checkBoundsForInsertion(index);

        int position = index + 1;
        Node<T> newNode = new Node<>(value, 0, 0);
        SnakeCell<T> newCell = newNode.getCell();
        int prevX;
        int prevY; 
        int dx;
        int dy;

        if(index==size){
            Node<T> newTail = new Node<>(value, 0, 0);
            Node<T> oldTail = getNodeAt(size);
            oldTail.setNextNode(newTail);

            if(size==1){
                newNode.cell.x = head.getCell().x + 1;
                newNode.cell.y = head.getCell().y;
            } 
            else {//extrapolate direction from last 2 nodes
                // get dx and dy from last 2 nodes
                dx = getNodeAt(size).getCell().x - getNodeAt(size-1).getCell().x;
                dy = getNodeAt(size).getCell().y - getNodeAt(size-1).getCell().y;

                // set new node's x to previous node's x + dx;
                newNode.cell.x = getNodeAt(size).getCell().x + dx;
                newNode.cell.y = getNodeAt(size).getCell().y + dy;
            } 
        }
        else { // insertion in middle or at head
            Node<T> oldNode = getNodeAt(position);
            // Node<T> newTail = getNodeAt(size);
            // newTail = new Node<>(null, 0 0);

            for (int i = size; i >= position; i--){
                Node<T> currentNode = getNodeAt(i);
                Node<T> nextNode = getNodeAt(i+1);

                nextNode.getCell().value = currentNode.getCell().value;
            }

            newNode = new Node(oldNode.getCell().x, oldNode.getCell(y), value);

            if(size==1){
                getNodeAt(size).getCell().x = getNodeAt(size-1).getCell().x + 1;
                getNodeAt(size).getCell().y = getNodeAt(size-1).getCell().y;
            }
            else {
                dx = getNodeAt(size).getCell().x - getNodeAt(size-1).getCell().x;
                dy = getNodeAt(size).getCell().y - getNodeAt(size-1).getCell().y;

                getNodeAt(size).getCell().x = getNodeAt(size-1).getCell().x + dx;
                getNodeAt(size).getCell().y = getNodeAt(size-1).getCell().y + dy;
            }
        }

        size++;

    }


    // ==============================================================================================

    /**
     * Removes the cell at the specified index from the body unless it is the 
     * only cell in the snake, in which case nothing happens.
     * The rest of the body is shifted accordingly towards the head.
     * 
     * @param index the zero-based position of the cell to remove
     * @throws IndexOutOfBoundsException if the index is invalid
     * @throws IllegalStateException if the snake is not initialized
     */
    @Override
    public void removeAt(int index){
        checkInitialization();
        checkBounds(index);

        if (size == 1) return;

        for (int i = index; i < size - 1; i++) {
            Node<T> currentNode = getNodeAt(i);
            Node<T> nextNode = getNodeAt(i+1);
            currentNode.setData(nextNode.getCell());
        }

        Node<T> tail = getNodeAt(size);
        tail = null;
        size--;

    }

    // ==============================================================================================

    /**
     * Returns the cell at the specified index in the body.
     * 
     * @param index the index to access
     * @return the SnakeCell at the given index
     * @throws IndexOutOfBoundsException if the index is invalid
     * @throws IllegalStateException if the snake is not initialized
     */
    public SnakeCell<T> getAt(int index){
        checkInitialization();
        checkBounds(index);

        return getNodeAt(index + 1).getCell();
    }

    // ==============================================================================================

    /**
     * Returns the current number of cells in the snake.
     * 
     * @return the length of the snake
     * @throws IllegalStateException if the snake is not initialized
     */
    public int length(){
        checkInitialization();
        return size;
    }



    // ==============================================================================================

    /**
     * Moves the snake in the given direction, updating all positions.
     * Valid directions are "U" (up), "D" (down), "L" (left), and "R" (right).
     * If the new head position would collide with any of the snake's existing cells 
     * (i.e., self-collision), the move is not performed and this method returns false.
     * 
     * @param direction the direction to move
     * @throws IllegalStateException if the snake is not initialized
     * @throws IllegalArgumentException if an invalid direction is given
     * @return true if the move was successful and no collision occurred;
     *         false if the move was aborted due to a collision with the
     *         snake's own body.
     */
    public boolean move(String direction){
        checkInitialization();

        if(!direction.equals("U") && !direction.equals("D") &&
           !direction.equals("L") && !direction.equals("R")){
            throw new IllegalArgumentException("Invalid direction");
        }

        return true;

    }







    // ==============================================================================================

    /**
     * Checks if the snake collides with a given coordinate.
     * 
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the snake occupies the given position, false otherwise
     * @throws IllegalStateException if the snake is not initialized
     */
    public boolean checkCollision(int x, int y){
        checkInitialization();

        return true;
    }





 // ==============================================================================================

private void checkInitialization(){
  if (!initialized){
    throw new IllegalStateException();
  }
}

// ==============================================================================================

private void checkBounds(int index){
  if((index<0) || (index >= size)) {
      throw new IndexOutOfBoundsException();
  }
}

// ==============================================================================================
private void checkBoundsForInsertion(int index){
  if((index<0) || (index > size)){
    throw new IndexOutOfBoundsException();
  }
}


public boolean isEmpty()
   {
      boolean result;

      if (size == 0) // Or getLength() == 0
      {
         assert head == null;
         result = true;
      }
      else
      {
         assert head != null;
         result = false;
      } // end if

      return result;
   } // end 


  

    public void add(int newPosition, SnakeCell<T> newEntry)
	{
 		if ((newPosition >= 1) && (newPosition <= size + 1))
		{
			Node<T> newNode = new Node(newEntry);
         
			if (newPosition == 1)                  // Case 1
			{
				newNode.setNextNode(head);
				head = newNode;
			}
			else									         // Case 2: list is not empty
			{                                      // and newPosition > 1
            Node<T> nodeBefore = getNodeAt(newPosition - 1);
            Node<T> nodeAfter = nodeBefore.getNextNode();
				newNode.setNextNode(nodeAfter);
				nodeBefore.setNextNode(newNode);
			} // end if
         
			size++;
		}
      else
         throw new IndexOutOfBoundsException("Illegal position given to add operation.");
   }


   public void add(SnakeCell<T> newEntry) 	      // OutOfMemoryError possible
	{
		Node<T> newNode = new Node(newEntry);

		if (isEmpty())
			head = newNode;
		else                              // Add to end of non-empty list
		{
			Node<T> tail = getNodeAt(size);
			tail.setNextNode(newNode); // Make last node reference new node
		} // end if	
		
		size++;
	} 


    public void setX(SnakeCell<T> cell, int x){
        cell.x = x;
    }

    public void setY(SnakeCell<T> cell, int y){
        cell.y = y;
}

}