package monticulo_binario;

public class MinHeap<T extends Comparable<T>> {

    private T[] heap;
    private int size;

    @SuppressWarnings("unchecked")
    //SuppressWarnings para evitar warnings de genéricos con arreglos
    public MinHeap(int capacidadInicial) {
        heap = (T[]) new Comparable[Math.max(4, capacidadInicial)];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public MinHeap(T[] datos) {
        heap = (T[]) new Comparable[Math.max(4, datos.length)];
        System.arraycopy(datos, 0, heap, 0, datos.length);
        size = datos.length;

        System.out.print("Arreglo inicial: ");
        mostrarHeapInline();

        int start = (size / 2) - 1;
        for (int i = start; i >= 0; i--) {
            System.out.println("\nHeapify desde índice " + i + " (valor " + heap[i] + "):");
            heapifyDownVerbose(i);
            System.out.print("Estado tras heapify(" + i + "): ");
            mostrarHeapInline();
        }

        System.out.println("\nHeap final tras heapify bottom-up:");
        printTree();
    }

    // ---------------- Métodos básicos ----------------

    public boolean isEmpty() { return size == 0; }

    public T peek() {
        if (isEmpty()) throw new IllegalStateException("El heap está vacío");
        return heap[0];
    }

    public static void heapsort(int[] arr) {
        Integer[] datos = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) datos[i] = arr[i];
        MinHeap<Integer> heap = new MinHeap<>(datos);
        for (int i = 0; i < arr.length; i++) arr[i] = heap.poll();
    }

    /** Inserta un nuevo valor en el heap */
    public void add(T valor) {
        if (size >= heap.length) {
            @SuppressWarnings("unchecked")
            T[] nuevo = (T[]) new Comparable[heap.length * 2];
            System.arraycopy(heap, 0, nuevo, 0, heap.length);
            heap = nuevo;
        }

        System.out.println("\nInsertando valor: " + valor);
        heap[size] = valor;
        size++;
        heapifyUpVerbose(size - 1);
        printTree();
        printArray(); // 👈 seguimiento del arreglo interno
    }

    /** Elimina y devuelve el elemento mínimo (raíz) */
    public T poll() {
        if (isEmpty()) throw new IllegalStateException("El heap está vacío");

        System.out.println("\nEliminando raíz (mínimo actual): " + heap[0]);
        System.out.print("Heap antes de eliminar: ");
        mostrarHeapInline();

        T min = heap[0];
        heap[0] = heap[size - 1];
        size--;

        heapifyDownVerbose(0);

        System.out.print("Heap después de percolateDown: ");
        mostrarHeapInline();
        printTree();
        printArray(); // 👈 seguimiento del arreglo interno
        System.out.println("--------------------------------");
        return min;
    }

    // ---------------- Métodos internos ----------------

    private void heapifyUpVerbose(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (heap[i].compareTo(heap[padre]) < 0) {
                System.out.println("[UP] Intercambio: " + heap[i] + " <-> " + heap[padre]);
                swap(i, padre);
                System.out.print("     Estado: ");
                mostrarHeapInline();
                i = padre;
            } else break;
        }
    }

    private void heapifyDownVerbose(int i) {
        while (true) {
            int izq = 2 * i + 1;
            int der = 2 * i + 2;
            int menor = i;

            if (izq < size && heap[izq].compareTo(heap[menor]) < 0) menor = izq;
            if (der < size && heap[der].compareTo(heap[menor]) < 0) menor = der;

            if (menor != i) {
                System.out.println("[DOWN] Intercambio: " + heap[i] + " <-> " + heap[menor]);
                swap(i, menor);
                System.out.print("        Estado: ");
                mostrarHeapInline();
                i = menor;
            } else break;
        }
    }

    // ---------------- Utilidades ----------------

    private void swap(int a, int b) {
        T tmp = heap[a];
        heap[a] = heap[b];
        heap[b] = tmp;
    }

    private void mostrarHeapInline() {
        System.out.print("[ ");
        for (int k = 0; k < size; k++) System.out.print(heap[k] + " ");
        System.out.println("]");
    }

    public void printTree() {
        System.out.println("Estructura del heap (por niveles):");
        if (size == 0) {
            System.out.println("(vacío)");
            return;
        }

        int nivel = 0, elementosEnNivel = 1, contador = 0;
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
            contador++;
            if (contador == elementosEnNivel) {
                System.out.println();
                nivel++;
                elementosEnNivel = (int) Math.pow(2, nivel);
                contador = 0;
            }
        }
        System.out.println();
    }

    // ---------------- Seguimiento del arreglo interno ----------------
    public void printArray() {
        System.out.print("Estado interno del arreglo: [ ");
        for (int i = 0; i < heap.length; i++) {
            if (i < size) System.out.print(heap[i] + " ");
            else System.out.print("_ "); // posición vacía
        }
        System.out.println("]");
    }
}
