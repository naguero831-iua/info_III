package monticulo_binario.integrador;

public class Tarea implements Comparable<Tarea> {
    private String nombre;
    private int prioridad;

    public Tarea(String nombre, int prioridad) {
        this.nombre = nombre;
        this.prioridad = prioridad;
    }

    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public int compareTo(Tarea otra) {
        // Para MinHeap: tareas con menor prioridad "vencen" primero
        return Integer.compare(this.prioridad, otra.prioridad);
    }

    @Override
    public String toString() {
        return nombre + " (Prioridad: " + prioridad + ")";
    }
}
