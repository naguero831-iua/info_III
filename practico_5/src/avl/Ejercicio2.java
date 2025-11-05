package avl;
/*Inserciones con rotación doble (caso LR y RL)
Inserte la secuencia: 30, 10, 20, 40, 35, 37.
a) Muestre el estado del árbol en cada paso.
b) Identifique los desbalances (FE = ±2).
c) Especifique cuándo corresponde rotación doble (LR o RL) y ejecútela.*/

public class Ejercicio2 {
      public static void main(String[] args) {
        Avl arbol = new Avl();
        int[] valores = {30, 10, 20, 40, 35, 37};

        
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

