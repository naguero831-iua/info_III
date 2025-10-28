package ejercicio_6;

import ejercicio_2.ListaEnlazada;

public class Main {
    public static void main(String[] args) {
        ListaEnlazada lista = new ListaEnlazada();

        // Crear la lista [1 -> 2 -> 3 -> 4 -> 5]
        lista.insertarFinal(1);
        lista.insertarFinal(2);
        lista.insertarFinal(3);
        lista.insertarFinal(4);
        lista.insertarFinal(5);

        // Contar los elementos de la lista
        System.out.println("Cantidad de elementos: " + lista.contar());
    }
}
