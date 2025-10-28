public class SumaElemArray {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5};
        int suma = sumarElementos(array, array.length);
        System.out.println("La suma de los elementos del array es: " + suma);
    }

    public static int sumarElementos(int[] arr, int n) {
        if (n <= 0) {
            return 0;
        } else {
            return arr[n - 1] + sumarElementos(arr, n - 1);
        }
    }
}