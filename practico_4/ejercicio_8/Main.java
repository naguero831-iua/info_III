package ejercicio_8;

import ejercicio_2.ListaEnlazada;

public class Main {
    public static void main(String[] args) {
        ListaEnlazada lista = new ListaEnlazada();

        // Crear la lista [1 -> 2 -> 4]
        lista.insertarFinal(1);
        lista.insertarFinal(2);
        lista.insertarFinal(4);

        // Insertar 3 en la posición 2
        lista.insertarEn(2, 3);

        // Imprimir la lista para verificar el resultado
        lista.imprimirLista();
    }
}
