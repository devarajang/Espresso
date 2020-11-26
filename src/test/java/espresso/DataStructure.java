/**
 * 
 */
package espresso;

/**
 * @author deva
 *
 */
class Node {
	int value;
	Node next;
	public Node(int value) {
		this.value=value;
	}
}

class LinkedList {
	
	Node head;
	
	LinkedList(int val) {
		this.head = new Node(val);
	}
	boolean addNode(int value) {
		Node lastNode = head;
		while(lastNode.next != null) {
			lastNode =lastNode.next;
		}
		lastNode.next = new Node(value);
		return true;
	}
	
	void print() {
		Node lastNode = head;
		while(lastNode != null) {
			System.out.print( lastNode.value + " --> " );
			lastNode =lastNode.next;
			
		}
		System.out.println("null");
	}
	
	void reverse() {
		Node next = head.next;
		Node previous = null;
		Node newHead = null;
		while(head != null) {
			next = head.next;
			head.next = previous;
			previous = head;
			newHead = previous;
			head = next;
			
		}
		head = newHead;
	}
}
public class DataStructure {

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedList list = new LinkedList(1);
		list.addNode(2);
		list.addNode(3);
		list.print();
		list.reverse();
		list.print();
		
	}

}
