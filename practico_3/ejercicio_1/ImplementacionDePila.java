package ejercicio_1;

public class ImplementacionDePila {
    private int maxSize;
    private int[] stackArray;
    private int top;

    public ImplementacionDePila(int size) {
        this.maxSize = size;
        this.stackArray = new int[maxSize];
        this.top = -1;
    }

    public void push(int value) {
        if (isFull()) {
            System.out.println("Stack is full. Cannot push " + value);
            return;
        }
        stackArray[++top] = value;
    }

    public int pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty. Cannot pop.");
            return -1; // Indicating stack is empty
        }
        return stackArray[top--];
    }

    public int top() {
        if (isEmpty()) {
            System.out.println("Stack is empty. Cannot retrieve top.");
            return -1; // Indicating stack is empty
        }
        return stackArray[top];
    }

    public boolean isEmpty() {
        return (top == -1);
    }

    public boolean isFull() {
        return (top == maxSize - 1);
    }

    public int size() {
        return top + 1;
    }

    
}
