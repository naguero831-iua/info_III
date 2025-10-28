package ejercicio_7;

import ejercicio_2.ListaEnlazada;

public class Main {
    public static void main(String[] args) {
        ListaEnlazada lista = new ListaEnlazada();

        // Crear la lista [10 -> 20 -> 30 -> 40]
        lista.insertarFinal(10);
        lista.insertarFinal(20);
        lista.insertarFinal(30);
        lista.insertarFinal(40);

        // Invertir la lista
        lista.invertir();

        // Imprimir la lista invertida
        lista.imprimirLista();
    }
}
