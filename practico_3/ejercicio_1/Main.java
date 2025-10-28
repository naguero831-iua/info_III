package ejercicio_1;

public class Main {
    public static void main(String[] args) {
        ImplementacionDePila stack = new ImplementacionDePila(5);

        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.push(40);

        System.out.println("Top element is: " + stack.top()); // Should print 40
        System.out.println("Stack size is: " + stack.size()); // Should print 4

        System.out.println("Popped element is: " + stack.pop()); // Should print 40
        System.out.println("Stack size after pop is: " + stack.size()); // Should print 3

     

        stack.push(40);
        stack.push(50);
        stack.push(60);
        stack.push(70); // Should indicate that the stack is full

        while (!stack.isEmpty()) {
            System.out.println("Popped element is: " + stack.pop());
            System.out.println("Stack size after pop is: " + stack.size()); // Should print decreasing sizes
        }

        stack.pop(); // Should indicate that the stack is empty
    }
}
