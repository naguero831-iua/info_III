package ejercicio_1;

public class Main {
    public static void main(String[] args) {
        // Crear tres nodos
        Nodo nodo1 = new Nodo(1);
        Nodo nodo2 = new Nodo(2);
        Nodo nodo3 = new Nodo(3);

        // Enlazar los nodos manualmente
        nodo1.setSiguiente(nodo2);
        nodo2.setSiguiente(nodo3);

        // Imprimir la lista completa
        Nodo actual = nodo1;
        while (actual != null) {
            System.out.println("Valor: " + actual.getValor());
            actual = actual.getSiguiente();
        }
    }
}
