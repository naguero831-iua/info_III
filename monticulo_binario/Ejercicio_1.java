package monticulo_binario;

/**
 * Programa principal para probar la clase MinHeap.
 */
public class Ejercicio_1 {
    public static void main(String[] args) {
        // Crear un heap con capacidad inicial 10
        MinHeap<Integer> heap = new MinHeap<>(10);
        //El <Integer> se realiza porque el minheap es genérico y puede almacenar cualquier tipo que implemente Comparable

        // Agregar valores
        heap.add(20);
        heap.add(5);
        heap.add(15);
        heap.add(3);
        heap.add(11);

        // Mostrar los elementos en orden de extracción (de menor a mayor)
        System.out.println("Elementos extraídos en orden (MinHeap):");
        while (!heap.isEmpty()) {
            System.out.print(heap.poll() + " ");
        }
        System.out.println(); // salto de línea final
    }
}
