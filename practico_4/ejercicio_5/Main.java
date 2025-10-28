package ejercicio_5;

import ejercicio_2.ListaEnlazada;

public class Main {
    public static void main(String[] args) {
        ListaEnlazada lista = new ListaEnlazada();

        // Crear la lista [5 -> 15 -> 25 -> 35]
        lista.insertarFinal(5);
        lista.insertarFinal(15);
        lista.insertarFinal(25);
        lista.insertarFinal(35);

        // Buscar el valor 25 (debe devolver true)
        System.out.println("¿Contiene 25? " + lista.buscar(25));

        // Buscar el valor 100 (debe devolver false)
        System.out.println("¿Contiene 100? " + lista.buscar(100));
    }
}
