public class NumFibonacci {
    public static void main(String[] args) {
        int n = 10; // Cambia este valor para obtener más o menos números de Fibonacci
        System.out.println("Los primeros " + n + " números de Fibonacci son:");
        for (int i = 0; i < n; i++) {
            System.out.print(fibonacci(i) + " ");
        }
    }

    public static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
}