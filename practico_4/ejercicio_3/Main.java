package ejercicio_3;

import ejercicio_2.ListaEnlazada;

public class Main {
    public static void main(String[] args) {
        ListaEnlazada lista = new ListaEnlazada();

        // Insertar valores al final
        lista.insertarFinal(1);
        lista.insertarFinal(2);
        lista.insertarFinal(3);

        // Imprimir la lista para verificar el orden
        lista.imprimirLista();
    }
}
