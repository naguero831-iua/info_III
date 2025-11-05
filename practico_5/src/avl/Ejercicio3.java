package avl;
/*Secuencia ordenada y “efecto peinar”
Inserte 5, 10, 15, 20, 25, 30, 35 en un ABB y luego balancee hasta AVL (o
inserte directamente en AVL, mostrando reequilibrios).
a) Explique por qué un ABB puro se desbalancea con datos crecientes.
b) Detalle las rotaciones que hacen que el AVL mantenga altura O(log n).*/

public class Ejercicio3 {
      public static void main(String[] args) {
        Abb arbol = new Abb();
        int[] valores = {5, 10, 15, 20, 25, 30, 35};

        /*Un ABB (Arbol Binario de Búsqueda) 
        puro se desbalancea con datos crecientes porque  
        al insertar elementos en orden ascendente, 
        la estructura del árbol se convierte en una lista enlazada,
        lo que provoca que las operaciones
        de búsqueda, inserción y eliminación 
        tengan un tiempo de ejecución O(n) en lugar de O(log n). */

        for(int i=0; i < valores.length; i++) {
            System.out.println("Insertando: " + valores[i]);
            arbol.insert(valores[i]);
            arbol.printTree();
            System.out.println("-----------------------");
            System.out.println("FE raíz: " + arbol.getRootBalance());
            System.out.println("Altura raíz: " + arbol.getAlturaTotal());
            System.out.println("-----------------------");
        }

    }
}

