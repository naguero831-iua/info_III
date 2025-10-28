package ejercicio_2;

public class ColaArreglo {
    private int maxSize;
    private Object[] queueArray;
    private int front;
    private int rear;
    private int nItems;

    public ColaArreglo(int size) {
        this.maxSize = size;
        this.queueArray = new Object[maxSize];
        this.front = 0;
        this.rear = -1;
        this.nItems = 0;
    }

    public void enqueue(Object value) {
        if (isFull()) {
            System.out.println("Queue is full. Cannot enqueue " + value);
            return;
        }
        if (rear == maxSize - 1) { // wrap around
            rear = -1;
        }
        queueArray[++rear] = value;
        nItems++;
    }

    public Object dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot dequeue.");
            return null; // Indicating queue is empty
        }
        Object temp = queueArray[front++];
        if (front == maxSize) { // wrap around
            front = 0;
        }
        nItems--;
        return temp;
    }

    public Object top() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot retrieve top.");
            return null; // Indicating queue is empty
        }
        return queueArray[front];
    }

    public boolean isEmpty() {
        return (nItems == 0);
    }

    public boolean isFull() {
        return (nItems == maxSize);
    }

    public int size() {
        return nItems;
    }
}
