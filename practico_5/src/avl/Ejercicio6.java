package avl;

/*Factor de equilibrio completo
Inserte 10, 100, 20, 80, 40, 70 (o una variante equivalente) y:
a) Liste para el árbol final (valor, altura, FE) de todos los nodos.
b) Marque los nodos críticos donde surgieron FE = ±2 durante el proceso. */
public class Ejercicio6 {
    public static void main(String[] args) {
        Avl arbol = new Avl();
        int[] valores = {10, 100, 20, 80, 40, 70};

        for (int v : valores) {
            arbol.insert(v);
        }

        System.out.println("\nÁrbol final (inorden):");
        listarNodos(arbol.getRoot());
    }

    public static void listarNodos(NodoAVL r) {
        if (r != null) {
            listarNodos(r.getIzq());

            int fe = (r.getIzq() != null ? r.getIzq().getAltura() : 0) 
                   - (r.getDer() != null ? r.getDer().getAltura() : 0);

            // Mostrar info del nodo
            System.out.println("Valor: " + r.getDato() + ", Altura: " + r.getAltura() + ", FE: " + fe);

            listarNodos(r.getDer());
        }
    }
}


    


