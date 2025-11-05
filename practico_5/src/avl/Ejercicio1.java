package avl;
/*Inserciones y FE paso a paso (caso LL y RR)
Inserte en un AVL la secuencia: 30, 20, 10, 40, 50, 60.
a) Dibuje el árbol tras cada inserción.
b) Calcule alturas y factor de equilibrio (FE) de cada nodo en cada paso.
c) Indique qué rotación se aplica en cada desbalance (LL o RR) y por qué.*/

public class Ejercicio1 {
      public static void main(String[] args) {
        Avl arbol = new Avl();
        int[] valores = {30, 20, 10, 40, 50, 60};

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

