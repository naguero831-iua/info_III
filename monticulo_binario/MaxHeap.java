package monticulo_binario;

public class MaxHeap {

    private int[] heap;
    private int size;

    public MaxHeap(int capacidadInicial) {
        heap = new int[Math.max(4, capacidadInicial)];
        size = 0;
    }

    /** Agrega un valor al heap (percolate up) */
    public void add(int valor) {
        if (size >= heap.length) {
            int[] nuevo = new int[heap.length * 2];
            System.arraycopy(heap, 0, nuevo, 0, heap.length);
            heap = nuevo;
        }

        System.out.println("\nInsertando valor " + valor + "...");
        heap[size] = valor;
        size++;
        heapifyUp(size - 1);
        printTree();
    }

    /** Extrae el valor máximo (raíz) */
    public int poll() {
        if (size == 0) throw new IllegalStateException("El heap está vacío");
        int max = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);
        return max;
    }

    /** Reordena hacia arriba */
    private void heapifyUp(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (heap[i] > heap[padre]) { // nota: '>' en lugar de '<'
                swap(i, padre);
                i = padre;
            } else break;
        }
    }

    /** Reordena hacia abajo */
    private void heapifyDown(int i) {
        while (true) {
            int izq = 2 * i + 1;
            int der = 2 * i + 2;
            int mayor = i;

            if (izq < size && heap[izq] > heap[mayor]) mayor = izq;
            if (der < size && heap[der] > heap[mayor]) mayor = der;

            if (mayor != i) {
                swap(i, mayor);
                i = mayor;
            } else break;
        }
    }

    public boolean isEmpty() { return size == 0; }


    private void swap(int a, int b) {
        int tmp = heap[a];
        heap[a] = heap[b];
        heap[b] = tmp;
    }

    public void printTree() {
        System.out.println("Estructura del MaxHeap:");
        if (size == 0) {
            System.out.println("(vacío)");
            return;
        }

        int nivel = 0, elementos = 1, cont = 0;
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
            cont++;
            if (cont == elementos) {
                System.out.println();
                nivel++;
                elementos = (int) Math.pow(2, nivel);
                cont = 0;
            }
        }
        System.out.println();
    }

}
