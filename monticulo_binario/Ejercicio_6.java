package monticulo_binario;

public class Ejercicio_6 {
    public static void main(String[] args) {
        int[] arr = {9, 4, 7, 1, 6, 2};

        System.out.println("Arreglo original:");
        mostrar(arr);

        // Llamar al método heapsort de tu clase MinHeap
        MinHeap.heapsort(arr); 
        //heapsort sirve para ordenar el arreglo usando un montículo mínimo

        System.out.println("\nArreglo ordenado final:");
        mostrar(arr);
    }

    private static void mostrar(int[] arr) {
        System.out.print("[ ");
        for (int v : arr) System.out.print(v + " ");
        System.out.println("]");
    }
}
