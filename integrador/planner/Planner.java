package integrador.planner;
import java.time.LocalDateTime;

/*
 * Clase Planner
 * --------------
 * Implementa un min-heap manual de objetos Recordatorio.
 * El heap mantiene siempre en la raíz (posición 0) el recordatorio más próximo
 * según su fecha. Sirve para obtener rápidamente cuál es el siguiente evento.

 */

public class Planner {
    private Recordatorio[] heap;  // Arreglo base que representa el heap binario
    private int size;             // Cantidad de elementos actualmente almacenados

    // Constructor: crea el heap con una capacidad inicial mínima de 4
    public Planner(int cap) {
        heap = new Recordatorio[Math.max(4, cap)];
        size = 0;
    }

    // ------------------------ Asegura capacidad ---------------------------------
    // Asegura que haya espacio suficiente en el arreglo antes de insertar.
    // Si está lleno, duplica su tamaño y copia los elementos existentes.
    // -------------------------------------------------------------------------
    private void ensureCapacity() { 
        if (size >= heap.length) {
            Recordatorio[] n = new Recordatorio[heap.length * 2];
            System.arraycopy(heap, 0, n, 0, heap.length); // copia más eficiente
            heap = n;
        }
    }

    // Intercambia dos posiciones dentro del heap.
    private void swap(int i, int j) { 
        Recordatorio t = heap[i];
        heap[i] = heap[j];
        heap[j] = t; 
    }

    // -------------------------------------------------------------------------
    // Operación "up-heap" (o "sift up"):
    // Se usa después de insertar un nuevo elemento.
    // Mientras el hijo sea menor que su padre (compareTo < 0),
    // se intercambian para restaurar la propiedad del min-heap.
    // -------------------------------------------------------------------------
    private void up(int i) {
        while (i > 0) {
            int p = (i - 1) / 2; // índice del padre
            if (heap[i].compareTo(heap[p]) < 0) { // si hijo < padre
                swap(i, p);
                i = p; // seguir subiendo
            } else break;
        }
    }

    // -------------------------------------------------------------------------
    // Operación "down-heap" (o "sift down"):
    // Se usa después de eliminar la raíz o modificar un elemento.
    // Compara el nodo con sus hijos y baja el menor hacia la raíz.
    // -------------------------------------------------------------------------
    private void down(int i) {
        while (true) {
            int l = 2 * i + 1; // hijo izquierdo
            int r = 2 * i + 2; // hijo derecho
            int s = i;         // índice del más pequeño (por ahora)
            
            if (l < size && heap[l].compareTo(heap[s]) < 0) s = l;
            if (r < size && heap[r].compareTo(heap[s]) < 0) s = r;

            if (s != i) {      // si alguno de los hijos es menor
                swap(i, s);    // intercambiar con el menor
                i = s;         // continuar bajando
            } else break;
        }
    }

    // -------------------------------------------------------------------------
    // Inserta un nuevo recordatorio en el heap.
    // Coloca el elemento al final y luego lo "sube" hasta su posición correcta.
    // -------------------------------------------------------------------------
    public void programar(Recordatorio r) {
        ensureCapacity();     // asegurarse de que haya espacio
        heap[size] = r;       // agregar al final
        size++;               // aumentar cantidad
        up(size - 1);         // reordenar hacia arriba
    }

    // -------------------------------------------------------------------------
    // Extrae y devuelve el recordatorio más próximo (la raíz del heap).
    // Mueve el último elemento a la raíz y luego lo "baja" para restaurar el orden.
    // -------------------------------------------------------------------------
    public Recordatorio proximo() {
        if (size == 0) return null;   // heap vacío
        Recordatorio r = heap[0];     // guardar raíz
        size--;                       // reducir tamaño
        if (size > 0) {
            heap[0] = heap[size];     // mover último elemento a la raíz
            down(0);                  // reordenar hacia abajo
        }
        heap[size] = null;            // liberar referencia para evitar fugas de memoria
        return r;
    }

    // -------------------------------------------------------------------------
    // Busca un recordatorio por su id y actualiza su fecha.
    // Luego ajusta su posición en el heap (puede subir o bajar según corresponda).
    // Complejidad O(n) por la búsqueda lineal.
    // -------------------------------------------------------------------------
    public void reprogramar(String id, LocalDateTime nueva) {
        if (id == null || nueva == null) return; // validar parámetros
        for (int i = 0; i < size; i++) {
            if (java.util.Objects.equals(heap[i].id, id)) {  // comparar ids de forma segura
                heap[i].fecha = nueva;    // actualizar fecha
                up(i);                    // ajustar hacia arriba
                down(i);                  // ajustar hacia abajo
                return;
            }
        }
    }

    // Devuelve la cantidad de recordatorios almacenados
    public int size() { return size; }

    // -------------------------------------------------------------------------
    // Imprime el contenido actual del heap (solo para depuración o pruebas).
    // -------------------------------------------------------------------------
    public void dump() {
        System.out.println("Planner heap:");
        for (int i = 0; i < size; i++)
            System.out.println("  " + heap[i]);
    }
}
