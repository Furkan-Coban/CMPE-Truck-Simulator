
public class MyLinkedList<T> {
    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;


    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void add(T data) {
        Node newNode = new Node(data);
        if (tail == null) {  // Checks whether list is empty
            head = newNode;  // If it is empty, head and tail should be the new node
            tail = newNode;
        } else {
            tail.next = newNode; // If not, newNode is added as tail.next then tail will be newNode
            tail = newNode;
        }
        size++;
    }

    public T poll() {
        if (head == null) return null;  // Checks whether list is empty
        T data = head.data;
        head = head.next;  // by moving the head to head.next, first element is removed.
        if (head == null) {tail = null;}
        size--;
        return data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
