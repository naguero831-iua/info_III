package avl;
/*Eliminación con rebalanceo
Dado el AVL resultante de insertar 50, 30, 70, 20, 40, 60, 80, 65, 75,
elimine: 20, luego 70.
a) Dibuje el árbol tras cada borrado.
b) Indique FE de los nodos afectados y rotaciones necesarias para restaurar el
balance.*/

public class Ejercicio4 {
      public static void main(String[] args) {
        Avl arbol = new Avl();
        int[] valores = {50, 30, 70, 20, 40, 60, 80, 65, 75};

        for(int i=0; i < valores.length; i++) {
            arbol.insert(valores[i]);
        }
        System.out.println("Árbol inicial:");
        arbol.printTree();  
        System.out.println("-----------------------");
        int[] valoresAEliminar = {20, 70};
        for(int i=0; i < valoresAEliminar.length; i++) {
            int valorAEliminar = valoresAEliminar[i];
            System.out.println("Eliminando: " + valorAEliminar);
            arbol.delete(valorAEliminar);
            arbol.printTree();
            System.out.println("-----------------------");
            System.out.println("FE raíz: " + arbol.getRootBalance());
            System.out.println("Altura raíz: " + arbol.getAlturaTotal());
            System.out.println("-----------------------");
        }
    }
}

