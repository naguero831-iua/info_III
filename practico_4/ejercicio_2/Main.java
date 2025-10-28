package ejercicio_2;

public class Main {
     public static void main(String[] args) {
        ListaEnlazada lista = new ListaEnlazada();

        // Insertar valores al inicio
        lista.insertarInicio(10);
        lista.insertarInicio(20);
        lista.insertarInicio(30);

        // Imprimir la lista para verificar el orden
        lista.imprimirLista();
    }
}
