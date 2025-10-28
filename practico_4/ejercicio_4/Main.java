package ejercicio_4;

import ejercicio_2.ListaEnlazada;

public class Main {
    public static void main(String[] args) {
        ListaEnlazada lista = new ListaEnlazada();

        // Crear la lista [10 -> 20 -> 30 -> 40]
        lista.insertarFinal(10);
        lista.insertarFinal(20);
        lista.insertarFinal(30);
        lista.insertarFinal(40);

        // Eliminar el nodo con valor 30
        lista.eliminar(30);

        // Imprimir la lista para verificar el resultado
        lista.imprimirLista();
    }
}
