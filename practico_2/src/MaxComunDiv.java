public class MaxComunDiv {
    public static void main(String[] args) {
        int a = 48;
        int b = 18;
        int mcd = calcularMCD(a, b);
        System.out.println("El máximo común divisor de " + a + " y " + b + " es: " + mcd);
    }

    // Método de Euclides para calcular el máximo común divisor (MCD)
    public static int calcularMCD(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return calcularMCD(b, a % b);
        }
    }
}
