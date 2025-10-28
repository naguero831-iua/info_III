package ejercicio_2;

public class Main {
    public static void main(String[] args) {
        ColaArreglo queue = new ColaArreglo(5);

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);

        System.out.println("Front element is: " + queue.top()); // Should print 1
        System.out.println("Queue size is: " + queue.size()); // Should print 4

        System.out.println("Dequeued element is: " + queue.dequeue()); // Should print 1
        System.out.println("Queue size after dequeue is: " + queue.size()); // Should print 3

        queue.enqueue(5);
        queue.enqueue(6);
        queue.enqueue(7); // Should indicate that the queue is full

        while (!queue.isEmpty()) {
            System.out.println("Dequeued element is: " + queue.dequeue());
            System.out.println("Queue size after dequeue is: " + queue.size()); // Should print decreasing sizes
        }

        queue.dequeue(); // Should indicate that the queue is empty
    }
}
