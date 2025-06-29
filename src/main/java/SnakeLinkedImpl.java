
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

        // private void setData(SnakeCell<T> newData)
        // {
        //     cell = newData;
        // } // end setData

        private Node<T> getNextNode()
        {
            return next;
        } // end getNextNode

        // private void setNextNode(Node<T> nextNode)
        // {
        //     next = nextNode;
        // } 
	}

    private Node<T> head;
    private  T[] pending;
    private int size = 0;
    private int pendingGrows;
    private boolean initialized;
    
    @SuppressWarnings("unchecked")
    public SnakeLinkedImpl(){
        head = null;
        size = 0;
        initialized = false;
        pending = (T[]) new Object[10];
        pendingGrows=0;
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
        head = new Node<>(headValue, x, y);
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
    @Override
    public void grow(T value){
        checkInitialization();
        pending[pendingGrows] = value;
        pendingGrows++;
        if (pending.length == pendingGrows) {
            @SuppressWarnings("unchecked")
            T[] temp = (T[]) new Object[pending.length*2];
            for(int i = 0; i<temp.length; i++){
                temp[i] = pending[i];
            }
            pending = temp;

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
    @Override
    public void insertAt(int index, T value){
        checkInitialization();
    
        int position = index + 1; //1-based position to insert value at

        if(index < 0 || index > size ){
            throw new IndexOutOfBoundsException("Invalid index");
        }

       
        Node<T> newNode;
        Node<T> oldTail = getNodeAt(size);
        Node<T> oldHead = getNodeAt(1);

        int dx;
        int dy;
        int prevX;
        int prevY;
        int newX;

        if(position==size+1){ //if inserting at end
            if (size==1){ //if snake is only head cell before inserting
                //get head cell's coords
                prevX = oldHead.getCell().x;
                prevY = oldHead.getCell().y;

                newNode = new Node<>(value, prevX+1, prevY); //new tail direction assumed to be to the right, x+1
                
                oldHead.next = newNode;
            }
            else{ // if inserting at very end, but snake has more than one cell

                dx = getNodeAt(size).getCell().x - getNodeAt(size-1).getCell().x;
                dy = getNodeAt(size).getCell().y - getNodeAt(size-1).getCell().y;

                newNode = new Node<>(value, oldTail.getCell().x + dx, oldTail.getCell().y + dy);
                oldTail.next = newNode;

            }
            size++;
        }
        else { // inserting at middle or at head
           Node<T> oldNode = getNodeAt(position);
           Node<T> prev = (position==1) ? null: getNodeAt(position-1);
           prevX = oldNode.getCell().x;
           prevY = oldNode.getCell().y;

           newNode = new Node<>(value, prevX, prevY);
           size++;

           if (prev == null){
                newNode.next = oldHead;
                head = newNode;
           }
           else {
                prev.next = newNode;
                newNode.next = oldNode;
           }
        }
           

        // shift coords towards tail
            for (int i = size; i > position; i--){
                Node<T> currentNode = getNodeAt(i);
                Node<T> prevNode = getNodeAt(i-1);

                currentNode.getCell().x = prevNode.getCell().x;
                currentNode.getCell().y = prevNode.getCell().y;
            }

           //tail extrapolation
            Node<T> newTail = getNodeAt(size);
            
            if(size==2){ 
                prevX = oldHead.getCell().x;
                prevY = oldHead.getCell().y;

                newNode = new Node<>(value, prevX, prevY);
                
                newX = prevX + 1;
                oldHead.getCell().x = newX;
                newNode.next = oldHead;
            }
            else{
                dx = getNodeAt(size-1).getCell().x - getNodeAt(size-2).getCell().x;
                dy = getNodeAt(size-1).getCell().y - getNodeAt(size-2).getCell().y;
                newTail.getCell().x = getNodeAt(size-1).getCell().x + dx;
                newTail.getCell().y = getNodeAt(size-1).getCell().y + dy;
            }
        
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

        //shift values from tail towards head, at desired position of removal
        for (int i = index+1; i < size; i++) {
            Node<T> currentNode = getNodeAt(i);
            Node<T> nextNode = getNodeAt(i+1);
            currentNode.getCell().value = nextNode.getCell().value;
        }

        //remove old tail after shifting
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
    @Override
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
    @Override
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
    @Override
    public boolean move(String direction){
        checkInitialization();

        
        for (int i = 0; i < pendingGrows; i++) {
                System.out.print(pending[i]);
            }
    

        int dx = 0;
        int dy = 0;
        if(direction.equals("U")){
            dy=1;
        }
        else if (direction.equals("D")){
            dy = -1;
        }
        else if (direction.equals("L")){
            dx = -1;
        }
        else if (direction.equals("R")){
            dx = 1;
        }
        else {
            throw new IllegalArgumentException();
        }

        
        Node<T> headNode = getNodeAt(1); //current head node before shifting
        int oldHeadX = headNode.getCell().x;
        int oldHeadY = headNode.getCell().y;

        int newX = headNode.getCell().x + dx;  //coords of new head after shift
        int newY = headNode.getCell().y + dy;


        // Check new head coords for collision with self    
        for(int j = 2; j <= size; j++){
                    if(getNodeAt(j).getCell().x == newX && getNodeAt(j).getCell().y == newY){
                        return false; // Collision detected, move aborted
                    }
                }

         
        //shift coords toward tail
        for(int i = size; i>=2; i--){

            Node<T> curr = getNodeAt(i);
            Node<T> prev = getNodeAt(i-1);
                
            if (i == 2){
                curr.getCell().x = oldHeadX;
                curr.getCell().y = oldHeadY;
            }
            else{
                curr.getCell().x = prev.getCell().x;
                curr.getCell().y = prev.getCell().y;
            }  
        }

        //get old tail and its coords
        Node<T> oldTail = getNodeAt(size);
        int oldTailX = oldTail.getCell().x;
        int oldTailY = oldTail.getCell().y;


        //set new head's coords to updated coords according to shift
        headNode.getCell().x = newX;
        headNode.getCell().y = newY;
    
        //if there is a pending grow, make that cell active
        if(pendingGrows > 0){
            //assign new tail with pending value and previous tail's coords
            Node<T> newTail = new Node<>(pending[0], oldTailX, oldTailY);
            oldTail.next = newTail;

            //update state of pendingGrows array
            for (int i = 0; i < pendingGrows - 1; i++) {
                pending[i] = pending[i + 1];
            }
            pending[pendingGrows - 1] = null;
            pendingGrows--;
            size++;
        }
        else{ //if no pending grow, remove last node to make shift consistent with size
            Node<T> beforeTail = getNodeAt(size-1);
            beforeTail.next = null;
        }

        return true; //if move successful, return true

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
    @Override
    public boolean checkCollision(int x, int y){
        checkInitialization();

        for(int i = 1; i <= size; i++){
            if (getNodeAt(i).getCell().x == x && getNodeAt(i).getCell().y == y){
                return true;
            }
        }
        return false;
    }

 // ==============================================================================================
//throw exception if snake is not initialized
private void checkInitialization(){
  if (!initialized){
    throw new IllegalStateException();
  }
}

// ==============================================================================================
//check if index is valid
private void checkBounds(int index){
  if((index<0) || (index >= size)) {
      throw new IndexOutOfBoundsException();
  }
}

// ==============================================================================================
// private void checkBoundsForInsertion(int position){
//   if((position<=0) || (position > size)){
//     throw new IndexOutOfBoundsException();
//   }
// }


// ==============================================================================================
//helper method, check if linked list is empty
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



// ==============================================================================================
//helper method, get node at given position (assuming linked list starts indexing at 1)

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
}