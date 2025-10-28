package ejercicio_5;

public class PalindromoColaPila {

    public static boolean esPalindromo(String str) {
        ColaArreglo cola = new ColaArreglo(str.length());
        PilaArreglo pila = new PilaArreglo(str.length());

        // Cola: izquierda a derecha, Pila: derecha a izquierda
        for (char c : str.toCharArray()) {
            cola.enqueue(c); // agrega al final (izquierda a derecha)
            pila.push(c);  // agrega al tope (derecha a izquierda)
        }

        // Comparar ambos extremos
        while (!cola.isEmpty() && !pila.isEmpty()) {
            char fromQueue = (char) cola.dequeue(); // saca de la izquierda
            char fromStack = (char) pila.pop();  // saca de la derecha
            if (fromQueue != fromStack) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        String testStr1 = "radar";
        String testStr2 = "hello";

        System.out.println(testStr1 + " is palindrome? " + esPalindromo(testStr1));
        System.out.println(testStr2 + " is palindrome? " + esPalindromo(testStr2));
    }
}
