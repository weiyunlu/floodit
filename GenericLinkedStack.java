/**
 *	Class for a generic stack using a linked list.
*/

public class GenericLinkedStack<T> implements Stack<T> {

	/**
    * Nested class implementing the linked list.
    */

	private static class Elem<E> {
		private E value;
		private Elem<E> next;
		public Elem(E value, Elem<E> next) {
			this.value = value;
			this.next = next;
		}
	}

	private Elem<T> top; // Instance variable

    /**
     * Check if the stack is empty
     * @return true or false
     */
	public boolean isEmpty() {
		return (top == null);
	}


	 /**
     * Push a new element onto the stack
     * 
     * @param value
     *            the value of the element to add
     */
	public void push(T value) {
		if (value == null) {
			throw new NullPointerException("Cannot push a null value onto the stack!");
		}

		Elem<T> newElem;

		if (top != null) {
			newElem = new Elem<T>(value, top);
		} else {
			newElem = new Elem<T>(value, null);
		}

		top = newElem;
	}

    /**
     * Constructor used for initializing the Frame
     * 
     * Peek at the top of the stack without removing it.
     *
     * @return top.value
     */
	public T peek() {
		if (top == null) {
			throw new EmptyStackException("Cannot peek at an empty stack.");
		}
		return top.value;
	}

    /**
     * Pop the top element from the stack.
     *
     * @return outElem
     *			the element at the top of the stack
     */
	public T pop() {
		if (top == null) {
			throw new EmptyStackException("Cannot pop an empty stack.");
		}
		T outElem = top.value;
		top = top.next;
		return outElem;
	}

}