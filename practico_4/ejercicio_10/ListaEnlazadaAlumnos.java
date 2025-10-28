package ejercicio_10;

public class ListaEnlazadaAlumnos {
    private class NodoAlumno {
        private String nombre;
        private int legajo;
        private NodoAlumno siguiente;

        public NodoAlumno(String nombre, int legajo) {
            this.nombre = nombre;
            this.legajo = legajo;
            this.siguiente = null;
        }
    }

    private NodoAlumno cabeza;

    public ListaEnlazadaAlumnos() {
        this.cabeza = null;
    }

    public void agregarAlumno(String nombre, int legajo) {
        NodoAlumno nuevo = new NodoAlumno(nombre, legajo);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
    }

    public boolean buscarAlumno(int legajo) {
        NodoAlumno actual = cabeza;
        while (actual != null) {
            if (actual.legajo == legajo) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public String buscarAlumnoDetalle(int legajo) {
        NodoAlumno actual = cabeza;
        while (actual != null) {
            if (actual.legajo == legajo) {
                return "Nombre: " + actual.nombre + ", Legajo: " + actual.legajo;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public void eliminarAlumno(int legajo) {
        if (cabeza == null) {
            return;
        }

        if (cabeza.legajo == legajo) {
            cabeza = cabeza.siguiente;
            return;
        }

        NodoAlumno actual = cabeza;
        while (actual.siguiente != null && actual.siguiente.legajo != legajo) {
            actual = actual.siguiente;
        }

        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
        }
    }

    public void imprimirLista() {
        NodoAlumno actual = cabeza;
        while (actual != null) {
            System.out.println("Nombre: " + actual.nombre + ", Legajo: " + actual.legajo);
            actual = actual.siguiente;
        }
    }
}
