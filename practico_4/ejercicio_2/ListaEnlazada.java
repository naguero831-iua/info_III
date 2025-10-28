package ejercicio_2;

import ejercicio_1.Nodo;

public class ListaEnlazada {
    private Nodo cabeza;

    public ListaEnlazada() {
        this.cabeza = null;
    }

    public void insertarInicio(int dato) {
        Nodo nuevoNodo = new Nodo(dato);
        nuevoNodo.setSiguiente(cabeza);
        cabeza = nuevoNodo;
    }

    public void insertarFinal(int dato) {
        Nodo nuevoNodo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
    }

    public void insertarEn(int pos, int valor) {
        if (pos < 0) {
            throw new IndexOutOfBoundsException("Posición inválida: " + pos);
        }

        Nodo nuevoNodo = new Nodo(valor);

        if (pos == 0) {
            nuevoNodo.setSiguiente(cabeza);
            cabeza = nuevoNodo;
            return;
        }

        Nodo actual = cabeza;
        for (int i = 0; i < pos - 1; i++) {
            if (actual == null) {
                throw new IndexOutOfBoundsException("Posición fuera de rango: " + pos);
            }
            actual = actual.getSiguiente();
        }

        nuevoNodo.setSiguiente(actual.getSiguiente());
        actual.setSiguiente(nuevoNodo);
    }

    public void imprimirLista() {
        Nodo actual = cabeza;
        while (actual != null) {
            System.out.println("Valor: " + actual.getValor());
            actual = actual.getSiguiente();
        }
    }

    public void eliminar(int valor) {
        if (cabeza == null) {
            return; // Lista vacía, no hay nada que eliminar
        }

        // Si el nodo a eliminar es el primero
        if (cabeza.getValor() == valor) {
            cabeza = cabeza.getSiguiente();
            return;
        }

        // Buscar el nodo a eliminar
        Nodo actual = cabeza;
        while (actual.getSiguiente() != null && actual.getSiguiente().getValor() != valor) {
            actual = actual.getSiguiente();
        }

        // Si se encontró el nodo, eliminarlo
        if (actual.getSiguiente() != null) {
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
        }
    }

    public boolean buscar(int valor) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.getValor() == valor) {
                return true; // Nodo encontrado
            }
            actual = actual.getSiguiente();
        }
        return false; // Nodo no encontrado
    }

    public int contar() {
        int contador = 0;
        Nodo actual = cabeza;
        while (actual != null) {
            contador++;
            actual = actual.getSiguiente();
        }
        return contador;
    }

    public void invertir() {
        Nodo anterior = null;
        Nodo actual = cabeza;
        Nodo siguiente;

        while (actual != null) {
            siguiente = actual.getSiguiente();
            actual.setSiguiente(anterior);
            anterior = actual;
            actual = siguiente;
        }

        cabeza = anterior;
    }

    public void eliminarDuplicados() {
        Nodo actual = cabeza;

        while (actual != null) {
            Nodo previo = actual;
            Nodo siguiente = actual.getSiguiente();

            while (siguiente != null) {
                if (actual.getValor() == siguiente.getValor()) {
                    previo.setSiguiente(siguiente.getSiguiente());
                } else {
                    previo = siguiente;
                }
                siguiente = siguiente.getSiguiente();
            }

            actual = actual.getSiguiente();
        }
    }
}
