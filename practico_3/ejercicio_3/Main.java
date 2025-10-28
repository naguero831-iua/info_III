package ejercicio_3;

public class Main {
    public static void main(String[] args) {
        String input = "Hola Mundo";
        PilaArreglo stack = new PilaArreglo(input.length());

        // Push each character onto the stack
        for (char c : input.toCharArray()) {
            stack.push(c);
        }

        // Pop characters from the stack to get the reversed string
        StringBuilder reversed = new StringBuilder();
        while (!stack.isEmpty()) {
            reversed.append((char) stack.pop());
        }

        System.out.println("Original: " + input);
        System.out.println("Reversed: " + reversed.toString());
    }
}
