package monticulo_binario;

public class Ejercicio_7 {
    public static void main(String[] args) {
        int[] datos = {10, 3, 15, 8, 6, 12};
        MaxHeap heap = new MaxHeap(datos.length);
        for (int x : datos) heap.add(x);

        System.out.println("\nOrden de eliminación (de mayor a menor):");
        while (true) {
            try {
                System.out.print(heap.poll() + " ");
            } catch (Exception e) { break; }
        }
        System.out.println();
    }
}
