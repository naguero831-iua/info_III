package ejercicio_1;


public class Main {
    public static void main(String[] args) {
        java.util.PriorityQueue<Integer> heap = new java.util.PriorityQueue<>();

        // Agregar los valores
        heap.add(20);
        heap.add(5);
        heap.add(15);
        heap.add(3);
        heap.add(11);

        // Mostrar los valores en orden de extracción (de menor a mayor)
        System.out.println("Elementos extraídos en orden:");
        while (!heap.isEmpty()) {
            System.out.print(heap.poll() + " ");
        }
        System.out.println();
    }
}
