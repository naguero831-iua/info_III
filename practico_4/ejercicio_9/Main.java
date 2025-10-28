package ejercicio_9;

import ejercicio_2.ListaEnlazada;

public class Main {
    public static void main(String[] args) {
        ListaEnlazada lista = new ListaEnlazada();

        // Crear la lista [1 -> 2 -> 2 -> 3 -> 1]
        lista.insertarFinal(1);
        lista.insertarFinal(2);
        lista.insertarFinal(2);
        lista.insertarFinal(3);
        lista.insertarFinal(1);

        // Eliminar duplicados
        lista.eliminarDuplicados();

        // Imprimir la lista para verificar el resultado
        lista.imprimirLista();
    }
}
