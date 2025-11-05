package ejercicio_1;


/**
 * Implementación básica de un montículo binario mínimo (MinHeap).
 * Almacena enteros en un arreglo, manteniendo el menor elemento en la raíz.
 */
public class Minheap {
    private int[] heap;   // Arreglo que contiene los elementos del heap
    private int size;     // Cantidad actual de elementos

    // Constructor: inicializa el heap con una capacidad inicial
    public Minheap(int capacidadInicial) {
        heap = new int[capacidadInicial];
        size = 0;

    }

    /** Devuelve true si el heap está vacío */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Devuelve el elemento mínimo sin eliminarlo */
    public int peek() {
        if (isEmpty()) throw new IllegalStateException("El heap está vacío");
        return heap[0];
    }

    /** Agrega un valor al heap */
    public void add(int valor) {
        // Si el arreglo está lleno, duplicar la capacidad
        if (size >= heap.length) {
            int[] nuevo = new int[heap.length * 2];
            for (int i = 0; i < heap.length; i++) {
                nuevo[i] = heap[i];
            }
            heap = nuevo;
        }

        // Insertar al final y hacer "flotar" (heapify-up)
        heap[size] = valor;
        size++;
        heapifyUp(size - 1);
    }

    /** Extrae y devuelve el elemento mínimo del heap */
    public int poll() {
        if (isEmpty()) throw new IllegalStateException("El heap está vacío");

        int min = heap[0];         // El mínimo es la raíz
        heap[0] = heap[size - 1];  // Mover el último elemento a la raíz
        size--;                    // Reducir tamaño
        heapifyDown(0);            // Reordenar hacia abajo

        return min;
    }

    /** Reordena hacia arriba (heapify-up) */
    private void heapifyUp(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (heap[i] < heap[padre]) {
                swap(i, padre);
                i = padre;
            } else break;
        }
    }

    /** Reordena hacia abajo (heapify-down) */
    private void heapifyDown(int i) {
        while (true) {
            int izq = 2 * i + 1;
            int der = 2 * i + 2;
            int menor = i;

            if (izq < size && heap[izq] < heap[menor]) menor = izq;
            if (der < size && heap[der] < heap[menor]) menor = der;

            if (menor != i) {
                swap(i, menor);
                i = menor;
            } else break;
        }
    }

    /** Intercambia dos posiciones del arreglo */
    private void swap(int a, int b) {
        int tmp = heap[a];
        heap[a] = heap[b];
        heap[b] = tmp;
    }
}
