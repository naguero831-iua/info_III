package ejercicio_6;

public class DeshacerRehacer {
    private PilaArreglo pilaDeshacer;
    private PilaArreglo pilaRehacer;

    public DeshacerRehacer(int size) {
        pilaDeshacer = new PilaArreglo(size);
        pilaRehacer = new PilaArreglo(size);
    }

    public void realizarAccion(int accion) {
        pilaDeshacer.push(accion);
        pilaRehacer = new PilaArreglo(pilaRehacer.size()); // Clear redo stack
    }

    public void deshacer() {
        if (!pilaDeshacer.isEmpty()) {
            int accion = pilaDeshacer.pop();
            pilaRehacer.push(accion);
            System.out.println("Deshizo la acción: " + accion);
        } else {
            System.out.println("No hay acciones para deshacer.");
        }
    }

    public void rehacer() {
        if (!pilaRehacer.isEmpty()) {
            int accion = pilaRehacer.pop();
            pilaDeshacer.push(accion);
            System.out.println("Rehizo la acción: " + accion);
        } else {
            System.out.println("No hay acciones para rehacer.");
        }
    }

    public void mostrarEstado() {
        System.out.print("Pila de Deshacer: ");
        pilaDeshacer.mostrarPila();
        System.out.print("Pila de Rehacer: ");
        pilaRehacer.mostrarPila();
    }


}
