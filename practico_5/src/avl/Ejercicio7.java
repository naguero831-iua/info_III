package avl;
/*Implementación guiada: rotación izquierda
Complete el código de una rotación simple a izquierda y úselo en insertar.
a) Muestre antes/después sobre un subárbol donde ocurra caso RR.
b) Actualice correctamente las alturas involucradas. */
public class Ejercicio7 {
    public static void main(String[] args) {
     Avl arbol = new Avl();

        // Inserciones que causan un caso RR
        System.out.println("---- Inserción 30 ----");
        arbol.insert(30);
        arbol.printTree();

        System.out.println("\n---- Inserción 40 ----");
        arbol.insert(40);
        arbol.printTree();

        System.out.println("\n---- Inserción 50 (debería provocar rotación izquierda) ----");
        arbol.insert(50);
        arbol.printTree();

        System.out.println("\nAltura total del árbol: " + arbol.getAlturaTotal());
        System.out.println("Factor de equilibrio de la raíz: " + arbol.getRootBalance());
    }
}
