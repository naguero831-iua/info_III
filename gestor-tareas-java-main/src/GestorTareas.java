package src;
public class GestorTareas{
    private Tarea[] tareas;
    private int contador;

    public GestorTareas(int capacidad) {
        this.tareas = new Tarea[capacidad];
        this.contador = 0;
    }

    public void agregarTarea(String descripcion) {
        if (contador < tareas.length) {
            tareas[contador] = new Tarea(descripcion);
            contador++;
        } else {
            System.out.println("No se pueden agregar más tareas. Capacidad máxima alcanzada.");
        }
    }

    public void marcarTareaComoCompletada(int indice) {
        if (indice >= 0 && indice < contador) {
            tareas[indice].marcarComoCompletada();
        } else {
            System.out.println("Índice de tarea inválido.");
        }
    }

    public void mostrarTareas() {
        for (int i = 0; i < contador; i++) {
            String estado = tareas[i].isEstado() ? "Completada" : "Pendiente";
            System.out.println((i + 1) + ". " + tareas[i].getDescripcion() + " - " + estado);
        }
    }


}