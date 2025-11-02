package integrador.planner;
import java.time.LocalDateTime;

/*
 Min-heap manual de Recordatorio[].
 Reprogramar (buscar por id) es O(n) — aceptable sin HashMap.
*/

// Removed Recordatorio class to comply with the rule of one public class per file.

public class Planner {
    private Recordatorio[] heap;
    private int size;
    public Planner(int cap) { heap = new Recordatorio[Math.max(4, cap)]; size = 0; }

    private void ensureCapacity() {
        if (size >= heap.length) {
            Recordatorio[] n = new Recordatorio[heap.length * 2];
            for (int i=0;i<heap.length;i++) n[i]=heap[i];
            heap = n;
        }
    }

    private void swap(int i, int j) { Recordatorio t = heap[i]; heap[i]=heap[j]; heap[j]=t; }

    private void up(int i) {
        while (i>0) {
            int p = (i-1)/2;
            if (heap[i].compareTo(heap[p]) < 0) { swap(i,p); i=p; } else break;
        }
    }

    private void down(int i) {
        while (true) {
            int l = 2*i+1, r = 2*i+2, s = i;
            if (l < size && heap[l].compareTo(heap[s]) < 0) s = l;
            if (r < size && heap[r].compareTo(heap[s]) < 0) s = r;
            if (s != i) { swap(i,s); i = s; } else break;
        }
    }

    public void programar(Recordatorio r) {
        ensureCapacity(); heap[size] = r; up(size); size++;
    }

    public Recordatorio proximo() {
        if (size==0) return null;
        Recordatorio r = heap[0];
        size--;
        if (size > 0) { heap[0] = heap[size]; down(0); }
        return r;
    }

    // Reprogramar: búsqueda O(n)
    public void reprogramar(String id, LocalDateTime nueva) {
        for (int i=0;i<size;i++) {
            if (heap[i].id.equals(id)) {
                heap[i].fecha = nueva;
                up(i); down(i);
                return;
            }
        }
    }

    public int size() { return size; }

    public void dump() {
        System.out.println("Planner heap:");
        for (int i=0;i<size;i++) System.out.println("  " + heap[i]);
    }
}
