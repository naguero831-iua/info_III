package ejercicio_1;

public class Main {
    public static void main(String[] args) {
        Minheap heap = new Minheap(10); // crear heap con capacidad inicial 10

        // Agregar elementos
        heap.add(20);
        heap.add(5);
        heap.add(15);
        heap.add(3);
        heap.add(11);

        // Mostrar elementos en orden de extracción
        System.out.println("Elementos extraídos en orden:");
        while (!heap.isEmpty()) {
            System.out.print(heap.poll() + " ");
        }
        System.out.println();
    }
}
